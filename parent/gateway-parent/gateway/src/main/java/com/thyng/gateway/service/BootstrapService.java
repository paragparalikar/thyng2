package com.thyng.gateway.service;

import java.util.ServiceLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.thyng.gateway.EventBus;
import com.thyng.gateway.client.EventPublisherClient;
import com.thyng.gateway.model.Context;
import com.thyng.gateway.provider.persistence.ConfigurationStore;
import com.thyng.gateway.provider.persistence.FilePersistenceProvider;
import com.thyng.gateway.provider.persistence.PersistenceProvider;
import com.thyng.gateway.provider.property.MutablePropertyProvider;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.gateway.service.health.HeartbeatService;
import com.thyng.gateway.service.health.StatusMonitoringService;
import com.thyng.model.RegistrationRequest;
import com.thyng.model.RegistrationResponse;
import com.thyng.model.dto.GatewayConfigurationDTO;
import com.thyng.netty.Client;
import com.thyng.netty.OioClient;
import com.thyng.util.Lambda;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BootstrapService extends CompositeService {
	
	private Context context;
	
	public BootstrapService() throws Exception {
		initShutdownHook();
		context = buildContext();
		add(new HeartbeatService(context));
		add(new StatusMonitoringService(context));
		add(new GatewayNettyServerService(context));
		add(new MetricsDispatchService(context));
		ServiceLoader.load(ServiceBuilder.class).forEach(builder -> add(builder.newInstance(context)));
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		stopExecutor();
	}
	
	private void stopExecutor(){
		if(null != context){
			try{
				context.getExecutor().shutdown();
				context.getExecutor().awaitTermination(3, TimeUnit.SECONDS);
			}catch(Exception e){
				log.error("Failed to stop executor gracefully", e);
			}
		}
	}
	
	private void initShutdownHook(){
		final Thread thread = new Thread("Gateway-Shutdown-Hook") {
			@Override
			public void run() {
				try {
					BootstrapService.this.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		Runtime.getRuntime().addShutdownHook(thread);
	}
	
	private Context buildContext() throws Exception{
		final PropertyProvider properties = new MutablePropertyProvider().load();
		final ThreadFactory threadFactory = runnable -> new Thread(runnable, "Gateway-executor-thread");
		final ScheduledExecutorService executor = Executors.newScheduledThreadPool
				(Runtime.getRuntime().availableProcessors(), threadFactory);
		final EventBus eventBus = new EventBus(executor);
		final PersistenceProvider persistenceProvider = new FilePersistenceProvider(properties);
		final Client rawClient = new OioClient(properties.getInteger("thyng.server.port", 9090), 
				properties.get("thyng.server.host", "localhost"));
		final Client client = new EventPublisherClient(rawClient, eventBus);
		final GatewayConfigurationDTO details =  getDetails(properties, client, persistenceProvider.getConfigurationStore());
		return Context.builder()
				.eventBus(eventBus)
				.executor(executor)
				.properties(properties)
				.details(details)
				.client(client)
				.persistenceProvider(persistenceProvider)
				.build();
	}
	
	private GatewayConfigurationDTO getDetails(
			final PropertyProvider properties, 
			final Client client,
			final ConfigurationStore configurationStore) throws Exception{
		try{
			return configurationStore.load().orElseGet(Lambda.uncheck(() -> {
				log.info("Gateway details not available locally");
				return configurationStore.save(fetch(properties, client));
			}));
		}catch(Exception e){
			log.error("Could not load details from persistence provider", e);
			return configurationStore.save(fetch(properties, client));
		}
	}
	
	private GatewayConfigurationDTO fetch(final PropertyProvider properties, final Client client) {
		log.info("Fetching gateway details from Thyng server");
		final RegistrationResponse response = client.execute(RegistrationRequest.builder()
				.gatewayId(properties.getLong("thyng.gateway.id", null))
				.host(properties.get("thyng.gateway.server.host", null))
				.port(properties.getInteger("thyng.gateway.server.port", null))
				.build());
		return response.getGatewayConfigurationDTO();
	}
		
}

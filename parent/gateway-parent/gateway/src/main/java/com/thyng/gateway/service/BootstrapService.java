package com.thyng.gateway.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.thyng.gateway.EventBus;
import com.thyng.gateway.client.ThyngClient;
import com.thyng.gateway.client.ThyngClientFactory;
import com.thyng.gateway.model.Context;
import com.thyng.gateway.provider.persistence.ConfigurationStore;
import com.thyng.gateway.provider.persistence.FilePersistenceProvider;
import com.thyng.gateway.provider.persistence.PersistenceProvider;
import com.thyng.gateway.provider.property.MutablePropertyProvider;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.gateway.provider.serialization.GsonSerializationProvider;
import com.thyng.gateway.provider.serialization.SerializationProvider;
import com.thyng.gateway.service.health.HeartbeatService;
import com.thyng.gateway.service.health.StatusMonitoringService;
import com.thyng.gateway.service.server.coap.CoapServerService;
import com.thyng.gateway.service.server.http.HttpServerService;
import com.thyng.model.Lambda;
import com.thyng.model.dto.GatewayConfigurationDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BootstrapService extends CompositeService {
	
	private Context context;
	
	public BootstrapService() throws Exception {
		initShutdownHook();
		context = buildContext();
		add(new HeartbeatService(context));
		add(new CoapServerService(context));
		add(new HttpServerService(context));
		add(new StatusMonitoringService(context));
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
		final Thread thread = new Thread(() -> {
			try {
				stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		Runtime.getRuntime().addShutdownHook(thread);
	}
	
	private Context buildContext() throws Exception{
		final PropertyProvider properties = new MutablePropertyProvider().load();
		final SerializationProvider<String> serializationProvider = new GsonSerializationProvider();
		final ScheduledExecutorService executor = Executors.newScheduledThreadPool
				(Runtime.getRuntime().availableProcessors());
		final EventBus eventBus = new EventBus(executor);
		final ThyngClient client = new ThyngClientFactory(eventBus, properties, serializationProvider).getClient();
		final PersistenceProvider persistenceProvider = new FilePersistenceProvider(properties, serializationProvider);
		
		final GatewayConfigurationDTO details =  getDetails(client, persistenceProvider.getConfigurationStore());
		return Context.builder()
				.eventBus(eventBus)
				.executor(executor)
				.properties(properties)
				.details(details)
				.client(client)
				.persistenceProvider(persistenceProvider)
				.serializationProvider(serializationProvider)
				.build();
	}
	
	private GatewayConfigurationDTO getDetails(final ThyngClient client,
			final ConfigurationStore configurationStore) throws Exception{
		try{
			return configurationStore.load().orElseGet(Lambda.uncheck(() -> {
				log.info("Gateway details not available locally, fetching from server");
				return configurationStore.save(client.registerAndFetchDetails());
			}));
		}catch(Exception e){
			log.error("Could not load details from persistence provider, fetching from server", e);
			return configurationStore.save(client.registerAndFetchDetails());
		}
	}
		
}

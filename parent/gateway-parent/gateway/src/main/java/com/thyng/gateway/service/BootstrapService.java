package com.thyng.gateway.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.thyng.gateway.EventBus;
import com.thyng.gateway.client.ThyngClient;
import com.thyng.gateway.client.ThyngClientBuilder;
import com.thyng.gateway.model.Context;
import com.thyng.gateway.provider.persistence.ConfigurationStore;
import com.thyng.gateway.provider.persistence.FilePersistenceProvider;
import com.thyng.gateway.provider.persistence.PersistenceProvider;
import com.thyng.gateway.provider.property.MutablePropertyProvider;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.gateway.service.health.HeartbeatService;
import com.thyng.gateway.service.health.StatusMonitoringService;
import com.thyng.gateway.service.message.MessageDispatchService;
import com.thyng.gateway.service.message.MessageNormalizationService;
import com.thyng.gateway.service.message.MessagePersistenceService;
import com.thyng.gateway.service.server.coap.CoapServerService;
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
		add(new MessageNormalizationService(context));
		add(new MessagePersistenceService(context));
		add(new MessageDispatchService(context));
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
		final ScheduledExecutorService executor = Executors.newScheduledThreadPool
				(Runtime.getRuntime().availableProcessors());
		final EventBus eventBus = new EventBus(executor);
		final ThyngClient client = new ThyngClientBuilder(eventBus, properties).getClient();
		final PersistenceProvider persistenceProvider = new FilePersistenceProvider(properties);
		final GatewayConfigurationDTO details =  getDetails(client, persistenceProvider.getConfigurationStore());
		return Context.builder()
				.eventBus(eventBus)
				.executor(executor)
				.properties(properties)
				.details(details)
				.client(client)
				.persistenceProvider(persistenceProvider)
				.build();
	}
	
	private GatewayConfigurationDTO getDetails(final ThyngClient client,
			final ConfigurationStore configurationStore) throws Exception{
		try{
			return configurationStore.load();
		}catch(Exception e){
			log.error("Could not load details from persistence provider, fetching from server");
			return configurationStore.save(client.registerAndFetchDetails());
		}
	}
		
}

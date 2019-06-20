package com.thyng.gateway.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.provider.client.ThyngClient;
import com.thyng.gateway.provider.client.ThyngClientBuilder;
import com.thyng.gateway.provider.details.DetailsProvider;
import com.thyng.gateway.provider.event.EventBus;
import com.thyng.gateway.provider.persistence.FilePersistenceProvider;
import com.thyng.gateway.provider.persistence.PersistenceProvider;
import com.thyng.gateway.provider.property.MutablePropertyProvider;
import com.thyng.gateway.provider.property.PropertyProvider;
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
		add(new DispatchService(context));
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
		final EventBus eventBus = new EventBus();
		final PropertyProvider properties = new MutablePropertyProvider().load();
		final ThyngClient client = new ThyngClientBuilder(properties).getClient();
		final ScheduledExecutorService executor = Executors.newScheduledThreadPool
				(Runtime.getRuntime().availableProcessors());
		final PersistenceProvider persistenceProvider = new FilePersistenceProvider(properties);
		final GatewayConfigurationDTO details =  new DetailsProvider(client, persistenceProvider).get();
		return Context.builder()
				.eventBus(eventBus)
				.executor(executor)
				.properties(properties)
				.details(details)
				.client(client)
				.persistenceProvider(persistenceProvider)
				.build();
	}
		
}

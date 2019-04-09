package com.thyng.gateway.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.provider.details.DetailsProvider;
import com.thyng.gateway.provider.event.EventBus;
import com.thyng.gateway.provider.persistence.FileSystemPersistenceProvider;
import com.thyng.gateway.provider.persistence.PersistenceProvider;
import com.thyng.gateway.provider.property.MutablePropertyProvider;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.gateway.service.server.coap.CoapServerService;
import com.thyng.model.dto.GatewayDetailsDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BootstrapService implements Service {
	
	private Context context;
	private CompositeService compositeService;
	
	@Override
	public void start() throws Exception {
		log.info("Bootstraping Thyng gateway...");
		initShutdownHook();
		context = buildContext();
		log.info("Thyng gateway context successfully built");
		compositeService = new CompositeService(
				new HeartbeatService(context),
				new CoapServerService(context)/*, 
				new DispatchService(context)*/);
		compositeService.start();
	}
	
	@Override
	public void stop() throws Exception {
		stopServices();
		stopExecutor();
	}
	
	private void stopServices(){
		if(null != compositeService){
			try{
				compositeService.stop();
			}catch(Exception e){
				log.error("Failed to stop services gracefully", e);
			}
		}
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
		final ScheduledExecutorService executor = Executors.newScheduledThreadPool
				(Runtime.getRuntime().availableProcessors());
		final GatewayDetailsDTO details = buildDetails(properties);
		final PersistenceProvider persistenceProvider = 
				new FileSystemPersistenceProvider(eventBus, properties);
		return Context.builder()
				.eventBus(eventBus)
				.executor(executor)
				.properties(properties)
				.details(details)
				.persistenceProvider(persistenceProvider)
				.build();
	}
	
	private GatewayDetailsDTO buildDetails(PropertyProvider propertyProvider) throws Exception{
		final DetailsProvider detailsProvider = new DetailsProvider(propertyProvider);
		try{
			return detailsProvider.load();
		}catch(Exception e){
			log.error("Could not load gateway details - "+e.getMessage());
			return detailsProvider.save(detailsProvider.fetch());
		}
	}
	
}

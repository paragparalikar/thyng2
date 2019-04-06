package com.thyng.gateway.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.thyng.gateway.Context;
import com.thyng.gateway.EventBus;
import com.thyng.gateway.GatewayDetailsProvider;
import com.thyng.gateway.MutablePropertyProvider;
import com.thyng.gateway.PropertyProvider;
import com.thyng.gateway.SimpleContext;
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
		compositeService = new CompositeService(new CoapServerService(context));
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
		final Gson gson = new Gson();
		final EventBus eventBus = new EventBus();
		final PropertyProvider properties = new MutablePropertyProvider().load();
		final ScheduledExecutorService executor = Executors.newScheduledThreadPool
				(Runtime.getRuntime().availableProcessors());
		final GatewayDetailsDTO details = buildDetails(gson, properties);
		return SimpleContext.builder()
				.gson(gson)
				.eventBus(eventBus)
				.executor(executor)
				.properties(properties)
				.details(details)
				.build();
	}
	
	private GatewayDetailsDTO buildDetails(Gson gson, PropertyProvider propertyProvider) throws Exception{
		final GatewayDetailsProvider detailsProvider = new GatewayDetailsProvider(gson, propertyProvider);
		try{
			return detailsProvider.load();
		}catch(Exception e){
			log.error("Could not load gateway details - "+e.getMessage());
			return detailsProvider.save(detailsProvider.fetch());
		}
	}
	
}

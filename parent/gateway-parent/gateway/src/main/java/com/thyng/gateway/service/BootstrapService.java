package com.thyng.gateway.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.thyng.gateway.Constant;
import com.thyng.gateway.EventBus;
import com.thyng.gateway.client.EventPublisherClient;
import com.thyng.gateway.model.Context;
import com.thyng.gateway.provider.persistence.ConfigurationStore;
import com.thyng.gateway.provider.persistence.FilePersistenceProvider;
import com.thyng.gateway.provider.persistence.PersistenceProvider;
import com.thyng.gateway.service.health.HeartbeatService;
import com.thyng.gateway.service.health.StatusMonitoringService;
import com.thyng.model.RegistrationRequest;
import com.thyng.model.RegistrationResponse;
import com.thyng.model.dto.GatewayConfigurationDTO;
import com.thyng.netty.Client;
import com.thyng.netty.OioClient;
import com.thyng.util.Lambda;
import com.thyng.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BootstrapService extends CompositeService {
	
	private Context context;
	
	public BootstrapService() throws Exception {
		initShutdownHook();
		
		final PropertyLoader propertyLoader = new PropertyLoader();
		propertyLoader.loadProperties();
		
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
		final ThreadFactory threadFactory = runnable -> new Thread(runnable, "Gateway-executor-thread");
		final ScheduledExecutorService executor = Executors.newScheduledThreadPool
				(Runtime.getRuntime().availableProcessors(), threadFactory);
		final EventBus eventBus = new EventBus(executor);
		final PersistenceProvider persistenceProvider = new FilePersistenceProvider();
		final Client rawClient = new OioClient(Integer.getInteger("thyng.server.port", 9090), 
				System.getProperty("thyng.server.host", "localhost"));
		final Client client = new EventPublisherClient(rawClient, eventBus);
		final GatewayConfigurationDTO details =  getDetails(client, persistenceProvider.getConfigurationStore());
		return Context.builder()
				.eventBus(eventBus)
				.executor(executor)
				.details(details)
				.client(client)
				.persistenceProvider(persistenceProvider)
				.build();
	}
	
	
	
	private GatewayConfigurationDTO getDetails(final Client client,
			final ConfigurationStore configurationStore) throws Exception{
		try{
			return configurationStore.load().orElseGet(Lambda.uncheck(() -> {
				log.info("Gateway details not available locally");
				return configurationStore.save(fetch(client));
			}));
		}catch(Exception e){
			log.error("Could not load details from persistence provider", e);
			return configurationStore.save(fetch(client));
		}
	}
	
	private GatewayConfigurationDTO fetch(final Client client) {
		log.info("Fetching gateway details from Thyng server");
		final RegistrationResponse response = client.execute(RegistrationRequest.builder()
				.gatewayId(Long.getLong("thyng.gateway.id", null))
				.host(System.getProperty("thyng.gateway.server.host", null))
				.port(Integer.getInteger("thyng.gateway.server.port", null))
				.build());
		return response.getGatewayConfigurationDTO();
	}
		
}

@Slf4j
class PropertyLoader{
	
	void loadProperties() throws IOException {
		final Properties properties = new Properties();
		loadDefaultProperties(properties);
		loadCustomProperties(properties);
		loadSystemProperties(properties);
		resolveSystemProperties(properties);
		replaceSystemProperties(properties);
	}
	
	private void replaceSystemProperties(final Properties properties) {
		System.getProperties().putAll(properties);
	}

	private void resolveSystemProperties(final Properties properties) {
		final String prefix = "system:";
		log.debug("Resolving properties with prefix \""+prefix+"\"");
		properties.stringPropertyNames().forEach(key -> {
			final String value = properties.getProperty(key);
			if(StringUtil.hasText(value) && value.startsWith(prefix)) {
				final String envKey = value.substring(prefix.length());
				final String envValue = System.getProperty(envKey, System.getenv(envKey));
				log.debug("Resolving " + key + " using " + envKey + " as " + envValue);
				properties.put(key, envValue);
			}
		});
	}
	
	private void loadSystemProperties(final Properties properties){
		log.debug("Loading properties - system");
		properties.putAll(System.getProperties());
	}
	
	private void loadDefaultProperties(final Properties properties) throws IOException{
		log.debug("Loading properties - default");
		properties.put(Constant.KEY_STORAGE, System.getProperty("user.home") 
				+ File.separator + "thyng" + File.separator);
		final String location = "/Thyng.properties";
		try(final InputStream in = BootstrapService.class.getResourceAsStream(location)){
			properties.load(in);
		}
	}
	
	private void loadCustomProperties(final Properties properties) throws IOException{
		log.debug("Loading properties - custom");
		final String location = System.getProperty("thyng.gateway.configuration.path");
		if(StringUtil.hasText(location)){
			try(final InputStream in = BootstrapService.class.getResourceAsStream(location)){
				properties.load(in);
				log.info("Properties loaded from "+location);
			}
		}
	}
	
}
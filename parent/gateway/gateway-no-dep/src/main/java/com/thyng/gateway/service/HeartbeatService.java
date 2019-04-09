package com.thyng.gateway.service;

import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.thyng.gateway.model.Constant;
import com.thyng.gateway.model.Context;
import com.thyng.gateway.provider.property.PropertyProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class HeartbeatService implements Service, Runnable {
	public static final String KEY_HEARTBEAT_URL = "thyng.server.url.heartbeat";
	public static final String KEY_HEARTBEAT_INTERVAL = "thyng.gateway.heartbeat.interval";
	
	private final Context context;
	private ScheduledFuture<?> future;
	
	@Override
	public void start() throws Exception {
		final int delay = context.getProperties().getInteger(KEY_HEARTBEAT_INTERVAL, 10);
		log.info("Starting heartbeat service width interval "+delay);
		future = context.getExecutor().scheduleWithFixedDelay(this, 0, delay, TimeUnit.SECONDS);
	}

	@Override
	public void run() {
		try{
			log.debug("Sending heartbeat");
			final PropertyProvider properties = context.getProperties();
			final Long id = context.getProperties().getLong(Constant.KEY_GATEWAY_ID, null);
			final byte[] value = ByteBuffer.allocate(Long.BYTES).putLong(id).array();
			final CoapResponse response = new CoapClient("coap", 
					properties.get(Constant.KEY_URL_SERVER, "www.thyng.io"),
					properties.getInteger(Constant.KEY_URL_SERVER_PORT, 5684),
					properties.get(KEY_HEARTBEAT_URL, "heartbeats"))
			.post(value, MediaTypeRegistry.APPLICATION_OCTET_STREAM);
			log.debug("Heartbeat response success : " + (null != response && response.isSuccess()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() throws Exception {
		if(null != future){
			log.info("Stopping heartbeat service");
			future.cancel(true);
		}
	}

}

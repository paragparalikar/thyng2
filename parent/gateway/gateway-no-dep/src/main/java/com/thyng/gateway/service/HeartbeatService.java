package com.thyng.gateway.service;

import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.thyng.gateway.model.Constant;
import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.Message;
import com.thyng.gateway.provider.property.PropertyProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class HeartbeatService implements Service, Runnable, Consumer<Message> {
	public static final String KEY_HEARTBEAT_URL = "thyng.server.url.heartbeat";
	public static final String KEY_HEARTBEAT_INTERVAL = "thyng.gateway.heartbeat.interval";
	
	private final Context context;
	private ScheduledFuture<?> future;
	private Long timestamp;
	private long delay;
	
	@Override
	public void start() throws Exception {
		delay = context.getProperties().getLong(KEY_HEARTBEAT_INTERVAL, 10000l);
		log.info("Starting heartbeat service width interval "+delay);
		context.getEventBus().register(Message.SENT, this);
		future = context.getExecutor().scheduleWithFixedDelay(this, 0, delay, TimeUnit.MILLISECONDS);
	}

	@Override
	public void accept(Message message) {
		this.timestamp = System.currentTimeMillis(); 
	}
	
	@Override
	public void run() {
		try{
			if(shouldBeat()){
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
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean shouldBeat(){
		return null == timestamp || delay <= (System.currentTimeMillis() - timestamp);
	}
	
	@Override
	public void stop() throws Exception {
		if(null != future){
			log.info("Stopping heartbeat service");
			future.cancel(true);
		}
	}

}

package com.thyng.gateway.service;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class HeartbeatService implements Service, Runnable, Consumer<Message> {
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
				context.getClient().heartbeat();
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
		context.getEventBus().unregister(Message.SENT, this);
		if(null != future){
			log.info("Stopping heartbeat service");
			future.cancel(true);
		}
	}

}

package com.thyng.gateway.service;

import java.net.HttpURLConnection;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.thyng.gateway.Constant;
import com.thyng.gateway.Context;
import com.thyng.gateway.util.HttpUtil;

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
			String url = context.getProperties().get(Constant.KEY_URL_SERVER, "http://www.thyng.io") 
					+ context.getProperties().get(KEY_HEARTBEAT_URL, "/gateways/%s/heartbeats");
			url = String.format(url, context.getProperties().get(Constant.KEY_GATEWAY_ID, null));
			
			final HttpURLConnection connection = HttpUtil.build(url, 
					context.getProperties().get(Constant.KEY_USERNAME, null),
					context.getProperties().get(Constant.KEY_PASSWORD, null));
			connection.setRequestMethod("HEAD");
			if(200 == connection.getResponseCode()){
				log.info("Successfully sent heartbeat");
			}else{
				log.error("Heartbeat not reaching server - Response Code : "+connection.getResponseCode() 
					+ ", Response Message : "+connection.getResponseMessage());
			}
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

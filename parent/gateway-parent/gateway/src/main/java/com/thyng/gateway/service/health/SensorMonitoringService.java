package com.thyng.gateway.service.health;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.SensorStatus;
import com.thyng.gateway.service.Service;
import com.thyng.model.dto.SensorDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SensorMonitoringService implements Service, Runnable, Consumer<Long>{
	private static final String TOPIC_MESSAGE_RECEIVED_PREFIX = "topic-message-received-sensor-";
	private static final String TOPIC_STATUS_CHANGED_PREFIX = "topic-sensor-status-changed-";
	
	public static String getTopicMessageReceived(Long sensorId){
		return TOPIC_MESSAGE_RECEIVED_PREFIX + sensorId;
	}
	
	public static String getTopicStatusChanged(Long sensorId){
		return TOPIC_STATUS_CHANGED_PREFIX + sensorId;
	}
	
	private Boolean active;
	private Long timestamp;
	private final Context context;
	private final SensorDTO sensor;
	private ScheduledFuture<?> future;
	
	@Override
	public void start() throws Exception {
		context.getEventBus().register(getTopicMessageReceived(sensor.getId()), this);
		future = context.getExecutor().scheduleAtFixedRate(this, 0, sensor.getInactivityPeriod(), TimeUnit.SECONDS);
	}
	
	@Override
	public void accept(Long timestamp) {
		try {
			this.timestamp = timestamp;
			if(null == this.active || !active){
				statusChanged();
			}
		} catch (Exception e) {
			log.error("Error in monitoring sensor health", e);
		}
	}
	
	private boolean isPastInactivityPeriod(){
		return null != timestamp && sensor.getInactivityPeriod()*1000 < System.currentTimeMillis() - timestamp;
	}
	
	private void statusChanged() throws Exception{
		active = !active;
		context.getEventBus().publish(getTopicStatusChanged(sensor.getId()), new SensorStatus(sensor, active));
		context.getClient().sendSensorStatus(sensor.getId(), active);
	}
	
	@Override
	public void run() {
		try{
			if(null != active && active && isPastInactivityPeriod()){
				statusChanged();
			}
		}catch(Exception e){
			log.error("Error in monitoring sensor health", e);
		}
	}
	
	@Override
	public void stop() throws Exception {
		context.getEventBus().unregister(getTopicMessageReceived(sensor.getId()), this);
		if(null != future){
			future.cancel(true);
		}
	}
	
}
package com.thyng.gateway.service;

import java.util.function.Consumer;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.Message;
import com.thyng.model.dto.ThingDetailsDTO;

public class StatusMonitoringService extends CompositeService implements Consumer<Message> {
	
	private final Context context;
	
	public StatusMonitoringService(Context context) {
		this.context = context;
		for(ThingDetailsDTO thing : context.getDetails().getThings()){
			add(new ThingMonitoringService(context, thing));
		}
	}
	
	@Override
	public void accept(Message message) {
		for(Long sensorId : message.getValues().keySet()){
			context.getEventBus().publish(SensorMonitoringService.getTopicMessageReceived(sensorId), message.getTimestamp());
		}
	}
	
	@Override
	public void start() throws Exception {
		super.start();
		context.getEventBus().register(Message.RECEIVED, this);
	}
	
	@Override
	public void stop() throws Exception {
		context.getEventBus().unregister(Message.RECEIVED, this);
		super.stop();
	}
}






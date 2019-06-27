package com.thyng.gateway.service.health;

import java.util.function.Consumer;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.TelemetryMessage;
import com.thyng.gateway.service.CompositeService;
import com.thyng.model.dto.ThingDetailsDTO;

public class StatusMonitoringService extends CompositeService implements Consumer<TelemetryMessage> {
	
	private final Context context;
	
	public StatusMonitoringService(Context context) {
		this.context = context;
		for(ThingDetailsDTO thing : context.getDetails().getThings()){
			add(new ThingMonitoringService(context, thing));
		}
	}
	
	@Override
	public void accept(TelemetryMessage message) {
		for(Long sensorId : message.getValues().keySet()){
			context.getEventBus().publish(SensorMonitoringService.getTopicMessageReceived(sensorId), message.getTimestamp());
		}
	}
	
	@Override
	public void start() throws Exception {
		super.start();
		context.getEventBus().register(TelemetryMessage.RECEIVED, this);
	}
	
	@Override
	public void stop() throws Exception {
		context.getEventBus().unregister(TelemetryMessage.RECEIVED, this);
		super.stop();
	}
}






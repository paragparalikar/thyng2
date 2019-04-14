package com.thyng.gateway.service;

import java.util.function.Consumer;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.Message;
import com.thyng.gateway.model.TelemetryRequest;
import com.thyng.gateway.provider.persistence.PersistentTelemetry;
import com.thyng.model.Lambda;
import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.ThingDetailsDTO;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class DispatchService implements Service, Consumer<Message> {
	

	private final Context context;
	
	@Override
	public void start() throws Exception {
		context.getEventBus().register(Message.PERSISTED, this);
	}

	@Override
	public void stop() throws Exception {
		context.getEventBus().unregister(Message.PERSISTED, this);
	}

	@Override
	@SneakyThrows
	public void accept(Message message) {
		for(Long sensorId : message.getValues().keySet()){
			final SensorDTO sensor = message.getSensor(sensorId);
			final Integer unsentCount = message.getUnsentCounts().get(sensorId);
			if(unsentCount >= sensor.getBatchSize()){
				final ThingDetailsDTO thing = message.getThing(sensorId);
				final PersistentTelemetry telemetry = context.getPersistenceProvider().getUnsentTelemetry(thing.getId(), sensorId);
				final TelemetryRequest telemetryRequest = TelemetryRequest.builder()
					.thingId(thing.getId())
					.sensorId(sensorId)
					.telemetry(telemetry)
					.success(() -> Lambda.uncheck(() -> success(telemetry)))
					.after(() -> after(message))
					.build();
				context.getClient().sendTelemetry(telemetryRequest);
			}
		}
	}
	
	private void success(PersistentTelemetry telemetry) throws Exception{
		context.getPersistenceProvider().markSent(telemetry);
	}
	
	private void after(Message message){
		context.getEventBus().publish(Message.SENT, message);
	}

}

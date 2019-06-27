package com.thyng.gateway.service.message;

import java.util.function.Consumer;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.Message;
import com.thyng.gateway.provider.persistence.TelemetryStore;
import com.thyng.gateway.service.Service;
import com.thyng.model.Telemetry;
import com.thyng.model.dto.SensorDTO;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MessageDispatchService implements Service, Consumer<Message> {
	
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
		message.getValues().keySet().forEach(sensorId -> {
			final SensorDTO sensor = context.getSensor(sensorId);
			final TelemetryStore telemetryStore = context
					.getPersistenceProvider()
					.getTelemetryStore(sensorId);
			synchronized(telemetryStore) {
				if(sensor.getBatchSize() <= telemetryStore.getCount()) {
					dispatch(telemetryStore);
				}
			}
		});
	}
	
	private void dispatch(final TelemetryStore telemetryStore) {
		final Telemetry telemetry = telemetryStore.read();
		try {
			context.getClient().send(telemetry);
		}catch(Throwable throwable) {
			log.error("Failed to dispatch telemetry to server\n" 
					+ throwable.getMessage(), throwable);
			telemetryStore.rollback(telemetry.getUuid());
		}
	}

}

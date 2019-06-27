package com.thyng.gateway.service.message;

import java.util.function.Consumer;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.Message;
import com.thyng.gateway.provider.persistence.TelemetryStore;
import com.thyng.gateway.service.Service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class MessagePersistenceService implements Service, Consumer<Message> {

private final Context context;
	
	@Override
	public void start() throws Exception {
		context.getEventBus().register(Message.NORMALIZED, this);
	}

	@Override
	public void stop() throws Exception {
		context.getEventBus().unregister(Message.NORMALIZED, this);
	}

	@Override
	@SneakyThrows
	public void accept(Message message) {
		message.getValues().forEach((sensorId, value) -> {
			final TelemetryStore telemetryStore = context.getPersistenceProvider()
					.getTelemetryStore(sensorId);
			telemetryStore.save(message.getTimestamp(), value);
		});
		context.getEventBus().publish(Message.PERSISTED, message);
	}

}

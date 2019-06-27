package com.thyng.gateway.service.message;

import java.util.function.Consumer;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.Message;
import com.thyng.gateway.service.Service;
import com.thyng.model.Lambda;
import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.ThingDetailsDTO;
import com.thyng.util.StringUtil;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class MessageNormalizationService implements Service, Consumer<Message> {
	private static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	
	private final Context context;
	
	@Override
	public void start() throws Exception {
		context.getEventBus().register(Message.RECEIVED, this);
	}

	@Override
	public void stop() throws Exception {
		context.getEventBus().unregister(Message.RECEIVED, this);
	}

	@Override
	@SneakyThrows
	public void accept(Message message) {
		message.getValues().replaceAll(Lambda.uncheck((sensorId, value) -> {
			final SensorDTO sensor = context.getSensor(sensorId);
			final ThingDetailsDTO thing = context.getThing(sensorId);
			if(StringUtil.hasText(sensor.getNormalizer())) {
				final ScriptEngine engine = scriptEngineManager.getEngineByName("nashorn");
				final Bindings bindings = engine.createBindings();
				bindings.put("thing", thing);
				bindings.put("sensor", sensor);
				bindings.put("value", value);
				final Object normalizedValue = engine.eval(sensor.getNormalizer(), bindings);
				return null == normalizedValue ? null : normalizedValue.toString();
			}
			return value;
		}));
		context.getEventBus().publish(Message.NORMALIZED, message);
	}

}

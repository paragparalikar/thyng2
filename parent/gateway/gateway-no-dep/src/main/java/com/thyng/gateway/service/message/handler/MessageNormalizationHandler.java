package com.thyng.gateway.service.message.handler;

import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.thyng.gateway.model.Message;
import com.thyng.gateway.service.message.MessageHandlerChain;
import com.thyng.model.dto.SensorDTO;
import com.thyng.util.StringUtil;

public class MessageNormalizationHandler implements MessageHandler {
	private static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	
	@Override
	public void handle(Message message, MessageHandlerChain chain) throws Exception {
		final Map<Long, Object> normalizedValues = new HashMap<>();
		for(Long sensorId : message.getValues().keySet()){
			final SensorDTO sensor = message.getSensor(sensorId);
			final String normalizer = sensor.getNormalizer();
			final Object value = message.getValues().get(sensorId);
			if(StringUtil.hasText(normalizer)){
				final ScriptEngine engine = scriptEngineManager.getEngineByName("nashorn");
				final Bindings bindings = engine.createBindings();
				bindings.put("message", message);
				bindings.put("sensor", sensor);
				bindings.put("value", message.getValues().get(sensorId));
				final Object normalizedValue = engine.eval(normalizer, bindings);
				if(null != normalizedValue){
					normalizedValues.put(sensorId, normalizedValue);
				}
			}else{
				normalizedValues.put(sensorId, value);
			}
		}
		message.getValues().clear();
		message.getValues().putAll(normalizedValues);
		chain.next(message);
	}
	
}

package com.thyng.gateway.metrics;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.ThingMetrics;
import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.ThingDetailsDTO;
import com.thyng.util.Lambda;
import com.thyng.util.StringUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ThingMetricsNormalizer {
	private static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

	private final Context context;
	
	public void normalize(ThingMetrics message) throws ScriptException {
		message.getValues().replaceAll(Lambda.uncheck((sensorId, value) -> {
			final SensorDTO sensor = context.getSensor(sensorId);
			final ThingDetailsDTO thing = context.getThing(sensorId);
			if(StringUtil.hasText(sensor.getNormalizer())) {
				final ScriptEngine engine = scriptEngineManager.getEngineByName("nashorn");
				final Bindings bindings = engine.createBindings();
				bindings.put("thing", thing);
				bindings.put("sensor", sensor);
				bindings.put("value", value);
				bindings.put("timestamp", message.getTimestamp());
				final Object normalizedValue = engine.eval(sensor.getNormalizer(), bindings);
				return null == normalizedValue ? null : (Double)normalizedValue;
			}
			return value;
		}));
	}
	
}

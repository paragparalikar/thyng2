package com.thyng.gateway.telemetry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.thyng.gateway.Constant;
import com.thyng.gateway.model.TelemetryMessage;
import com.thyng.util.StringUtil;

public class TelemetryMessageResolver {

	public TelemetryMessage resolve(final String payload) {
		final Map<Long, Double> values = new HashMap<>();
		Arrays.asList(payload.split(Constant.LINEFEED))
			.forEach(line -> {
				if(StringUtil.hasText(line)) {
					final String[] tokens = line.split(",");
					if(2 <= tokens.length) {
						values.put(Long.parseLong(tokens[0]), Double.parseDouble(tokens[1]));
					}
				}
			});
		return TelemetryMessage.builder()
				.timestamp(System.currentTimeMillis())
				.values(values)
				.build();
	}

}

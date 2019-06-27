package com.thyng.gateway.service.server.coap;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.thyng.gateway.model.Constant;
import com.thyng.util.StringUtil;

public class TelemetryPayloadResolver {

	public Map<Long, String> resolve(final byte[] payload) throws UnsupportedEncodingException{
		final Map<Long, String> values = new HashMap<>();
		Arrays.asList(new String(payload,Constant.CHARSET).split(Constant.LINEFEED))
			.forEach(line -> {
				if(StringUtil.hasText(line)) {
					final String[] tokens = line.split(",");
					if(2 <= tokens.length) {
						values.put(Long.parseLong(tokens[0]), tokens[1]);
					}
				}
			});
		return values;
	}

}

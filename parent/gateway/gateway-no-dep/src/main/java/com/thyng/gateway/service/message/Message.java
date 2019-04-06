package com.thyng.gateway.service.message;

import java.util.HashMap;
import java.util.Map;

import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.ThingDetailsDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Message {

	private Long timestamp;
	
	private byte[] payload;
	
	private SensorDTO sensor;
	
	private ThingDetailsDTO thing;
	
	private final Map<Long, Object> values = new HashMap<>();
	
}

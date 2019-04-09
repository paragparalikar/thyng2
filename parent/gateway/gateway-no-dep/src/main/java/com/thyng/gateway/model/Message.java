package com.thyng.gateway.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.ThingDetailsDTO;

public class Message {
	
	private final long timestamp;
	
	private final byte[] payload;
	
	private final MessageContext messageContext;
		
	private final Map<Long, Object> values = new HashMap<>();
	
	private final Map<Long, Integer> unsentCounts = new HashMap<>();
	
	public Message(long timestamp, byte[] payload, MessageContext messageContext) {
		Objects.requireNonNull(payload);
		Objects.requireNonNull(messageContext);
		this.timestamp = timestamp;
		this.payload = payload;
		this.messageContext = messageContext;
	}

	public long getTimestamp(){
		return timestamp;
	}
	
	public byte[] getPayload(){
		return payload;
	}
	
	public Map<Long, Object> getValues(){
		return values;
	}
	
	public Map<Long, Integer> getUnsentCounts(){
		return unsentCounts;
	}
	
	public ThingDetailsDTO getThing(Long sensorId){
		return messageContext.getThing(sensorId);
	}
	
	public SensorDTO getSensor(Long sensorId){
		return messageContext.getSensor(sensorId);
	}
	
}

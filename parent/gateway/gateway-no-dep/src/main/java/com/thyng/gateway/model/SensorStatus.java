package com.thyng.gateway.model;

import com.thyng.model.dto.SensorDTO;

import lombok.Value;

@Value
public class SensorStatus{
	private final SensorDTO sensor;
	private final Boolean active;
}
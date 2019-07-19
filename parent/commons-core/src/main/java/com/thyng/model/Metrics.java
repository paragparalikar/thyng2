package com.thyng.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metrics {

	private Object meta;
	private Double value;
	private Long sensorId;
	private Long timestamp;
	
}

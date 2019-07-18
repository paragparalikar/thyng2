package com.thyng.model;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ThingStatus {

	private Long id;
	private String name;
	private Boolean active;
	private Date statusChangeTimestamp;

}

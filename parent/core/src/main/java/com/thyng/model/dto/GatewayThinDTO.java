package com.thyng.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of={"id","name"})
public class GatewayThinDTO {

	private Long id;

	private String name;

}

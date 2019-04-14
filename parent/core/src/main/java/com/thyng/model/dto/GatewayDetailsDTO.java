package com.thyng.model.dto;

import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class GatewayDetailsDTO extends GatewayDTO {

	private Map<String,String> properties;

}

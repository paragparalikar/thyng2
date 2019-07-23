package com.thyng.domain.gateway;

import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true, exclude="properties")
public class GatewayDetailsDTO extends GatewayDTO {
	private static final long serialVersionUID = -8833621156389851017L;

	private Map<String,String> properties;

}

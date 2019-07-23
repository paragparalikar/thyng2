package com.thyng.domain.gateway;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of={"id","name"})
public class GatewayThinDTO implements Serializable {
	private static final long serialVersionUID = 5964186100036483482L;

	private Long id;

	private String name;

}

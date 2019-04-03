package com.thyng.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayRegistration {

	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne
	private Gateway gateway;
	
	private String host;
	
	private Integer port;

}

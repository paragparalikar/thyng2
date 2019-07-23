package com.thyng.domain.tenant;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import lombok.Data;

@Data
public class TenantDTO implements Serializable {
	private static final long serialVersionUID = 811520489186816667L;

	private Long id;
	
	private String name;
	
	private String description;
	
	private Date start;
	
	private Date expiry;
	
	private boolean locked;

	private Map<String, String> properties;
	
}

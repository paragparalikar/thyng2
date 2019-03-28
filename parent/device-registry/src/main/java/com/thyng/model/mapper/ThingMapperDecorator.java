package com.thyng.model.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.thyng.model.dto.ThingDetailsDTO;
import com.thyng.model.entity.Thing;

public abstract class ThingMapperDecorator implements ThingMapper {

	@Autowired
    @Qualifier("delegate")
	private ThingMapper delegate;
	
	@Override
	public Thing toEntity(ThingDetailsDTO dto) {
		final Thing thing = delegate.toEntity(dto);
		if(null != thing.getMetrics()){
			thing.getMetrics().forEach(metric -> {
				metric.setThing(thing);
			});
		}
		return thing;
	}

}

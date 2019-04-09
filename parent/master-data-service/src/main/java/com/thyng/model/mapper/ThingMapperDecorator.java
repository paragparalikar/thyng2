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
	public Thing entity(ThingDetailsDTO dto) {
		final Thing thing = delegate.entity(dto);
		if(null != thing.getSensors()){
			thing.getSensors().forEach(sensor -> {
				sensor.setThing(thing);
			});
		}
		return thing;
	}

}

package com.thyng.model.mapper;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.Mapper;

import com.thyng.entity.Thing;
import com.thyng.model.ThingStatus;

@Mapper(componentModel = "spring")
public interface ThingStatusMapper {

	ThingStatus toThingStatus(Thing thing);
	
	Set<ThingStatus> toThingStatuses(Collection<Thing> things);
	
}

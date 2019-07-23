package com.thyng.domain.actuator;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActuatorMapper {

	ActuatorDTO toDTO(Actuator actuator);

	Set<ActuatorDTO> toDTO(Collection<? extends Actuator> actuators);

	@InheritInverseConfiguration
	Actuator toEntity(ActuatorDTO dto);

}

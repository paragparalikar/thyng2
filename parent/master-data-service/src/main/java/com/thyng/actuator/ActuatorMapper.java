package com.thyng.actuator;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.thyng.model.dto.ActuatorDTO;

@Mapper(componentModel = "spring")
public interface ActuatorMapper {

	ActuatorDTO toDTO(Actuator actuator);

	Set<ActuatorDTO> toDTO(Collection<? extends Actuator> actuators);

	@InheritInverseConfiguration
	Actuator toEntity(ActuatorDTO dto);

}

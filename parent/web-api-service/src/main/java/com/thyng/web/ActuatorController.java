package com.thyng.web;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.domain.actuator.Actuator;
import com.thyng.domain.actuator.ActuatorDTO;
import com.thyng.domain.actuator.ActuatorMapper;
import com.thyng.domain.actuator.ActuatorService;
import com.thyng.domain.thing.Thing;
import com.thyng.domain.thing.ThingService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/actuators")
public class ActuatorController {
 
	private final ThingService thingService;
	private final ActuatorMapper actuatorMapper;
	private final ActuatorService actuatorService;
	
	@GetMapping
	public Set<ActuatorDTO> findByThingId(@RequestParam @NonNull @Positive final Long thingId){
		return actuatorMapper.toDTO(actuatorService.findByThingId(thingId));
	}
	
	@GetMapping("/{actuatorId}")
	public ActuatorDTO findById(@PathVariable @NonNull @Positive final Long actuatorId) {
		return actuatorMapper.toDTO(actuatorService.findById(actuatorId));
	}
	
	@PostMapping
	public ActuatorDTO create(@RequestParam @NonNull @Positive final Long thingId, 
			@RequestBody @NonNull @Valid final ActuatorDTO dto) {
		final Thing thing = thingService.findById(thingId);
		final Actuator actuator = actuatorMapper.toEntity(dto);
		actuator.setThing(thing);
		return actuatorMapper.toDTO(actuatorService.create(actuator));
	}
	
	@PutMapping
	public ActuatorDTO update(@RequestParam @NonNull @Positive final Long thingId, 
			@RequestBody @NonNull @Valid final ActuatorDTO dto) {
		final Thing thing = thingService.findById(thingId);
		final Actuator actuator = actuatorMapper.toEntity(dto);
		actuator.setThing(thing);
		return actuatorMapper.toDTO(actuatorService.update(actuator));
	}
	
	@DeleteMapping("/{actuatorId}")
	public void delete(@PathVariable @NonNull @Positive final Long actuatorId) {
		actuatorService.delete(actuatorId);
	}

	@GetMapping(params={"id", "name", "thingId"})
	public String existsByIdNotAndNameAndTenantId(@RequestParam(defaultValue="0") final Long id, 
			@RequestParam final String name, @RequestParam final Long thingId){
		return actuatorService.existsByIdNotAndNameAndThingId(id, name, thingId) ? "[\"This name is already taken\"]" : Boolean.TRUE.toString();
	}
}

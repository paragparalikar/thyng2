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

import com.thyng.domain.sensor.Sensor;
import com.thyng.domain.sensor.SensorDTO;
import com.thyng.domain.sensor.SensorMapper;
import com.thyng.domain.sensor.SensorService;
import com.thyng.domain.thing.Thing;
import com.thyng.domain.thing.ThingService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sensors")
public class SensorController {

	private final ThingService thingService;
	private final SensorMapper sensorMapper;
	private final SensorService sensorService;
	
	@GetMapping
	public Set<SensorDTO> findByThingId(@RequestParam @NonNull @Positive final Long thingId){
		return sensorMapper.toDTO(sensorService.findByThingId(thingId));
	}
	
	@GetMapping("/{sensorId}")
	public SensorDTO findById(@PathVariable @NonNull @Positive final Long sensorId) {
		return sensorMapper.toDTO(sensorService.findById(sensorId));
	}
	
	@PostMapping
	public SensorDTO create(@RequestParam @NonNull @Positive final Long thingId, 
			@RequestBody @NonNull @Valid final SensorDTO dto) {
		final Thing thing = thingService.findById(thingId);
		final Sensor sensor = sensorMapper.toEntity(dto);
		sensor.setThing(thing);
		return sensorMapper.toDTO(sensorService.create(sensor));
	}
	
	@PutMapping
	public SensorDTO update(@RequestParam @NonNull @Positive final Long thingId, 
			@RequestBody @NonNull @Valid final SensorDTO dto) {
		final Thing thing = thingService.findById(thingId);
		final Sensor sensor = sensorMapper.toEntity(dto);
		sensor.setThing(thing);
		return sensorMapper.toDTO(sensorService.update(sensor));
	}
	
	@DeleteMapping("/{sensorId}")
	public void delete(@PathVariable @NonNull @Positive final Long sensorId) {
		sensorService.delete(sensorId);
	}

	@GetMapping(params={"id", "name", "thingId"})
	public String existsByIdNotAndNameAndTenantId(@RequestParam(defaultValue="0") Long id, @RequestParam String name, @RequestParam Long thingId){
		return sensorService.existsByIdNotAndNameAndThingId(id, name, thingId) ? "[\"This name is already taken\"]" : Boolean.TRUE.toString();
	}
}

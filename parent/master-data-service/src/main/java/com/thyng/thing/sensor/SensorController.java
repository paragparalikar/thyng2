package com.thyng.thing.sensor;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.model.dto.SensorDTO;
import com.thyng.thing.Thing;
import com.thyng.thing.ThingService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/things/{thingId}/sensors")
public class SensorController {

	private final ThingService thingService;
	private final SensorMapper sensorMapper;
	private final SensorService sensorService;
	
	@GetMapping
	public Set<SensorDTO> findByThingId(@PathVariable @Positive final Long thingId){
		return sensorMapper.toDTO(sensorService.findByThingId(thingId));
	}
	
	@PostMapping
	public void create(@PathVariable @Positive final Long thingId, @RequestBody @NonNull @Valid final SensorDTO dto) {
		final Thing thing = thingService.findById(thingId);
		final Sensor sensor = sensorMapper.toEntity(dto);
		sensor.setThing(thing);
		sensorService.save(sensor);
	}
	
	@DeleteMapping("/{sensorId}")
	public void delete(@PathVariable @Positive final Long thingId, @PathVariable @Positive final Long sensorId) {
		sensorService.delete(sensorId);
	}

}
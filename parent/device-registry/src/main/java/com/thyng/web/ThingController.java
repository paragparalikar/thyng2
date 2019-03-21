package com.thyng.web;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.model.dto.ThingDTO;
import com.thyng.model.entity.Thing;
import com.thyng.model.mapper.ThingMapper;
import com.thyng.service.ThingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/things")
public class ThingController {

	private final ThingMapper thingMapper;
	private final ThingService thingService;
	
	@GetMapping
	public Set<ThingDTO> findAll(){
		return thingMapper.toDTO(thingService.findAll());
	}
	
	@GetMapping("/{id}")
	public ThingDTO findById(@PathVariable Long id){
		return thingMapper.toDTO(thingService.findById(id));
	}
	
	@PostMapping
	@ResponseBody
	public ThingDTO save(@RequestBody @Valid ThingDTO dto){
		final Thing thing = thingMapper.toEntity(dto);
		final Thing managedThing = thingService.save(thing);
		return thingMapper.toDTO(managedThing);
	}
	
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable Long id){
		thingService.deleteById(id);
	}

}

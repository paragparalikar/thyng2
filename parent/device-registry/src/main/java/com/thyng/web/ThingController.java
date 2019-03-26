package com.thyng.web;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.model.dto.ThingDTO;
import com.thyng.model.dto.ThingDetailsDTO;
import com.thyng.model.entity.Thing;
import com.thyng.model.entity.User;
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
	public Set<ThingDTO> findAll(@AuthenticationPrincipal User user){
		return thingMapper.toDTO(thingService.findByTenantId(user.getTenant().getId()));
	}
	
	//TODO modify all below methods to take tenant into concideration
	@GetMapping("/{id}")
	public ThingDetailsDTO findById(@PathVariable Long id, @AuthenticationPrincipal User user){
		return thingMapper.toDTO(thingService.findById(id));
	}
	
	@PostMapping
	@ResponseBody
	public ThingDetailsDTO save(@RequestBody @Valid ThingDetailsDTO dto, @AuthenticationPrincipal User user){
		final Thing thing = thingMapper.toEntity(dto);
		final Thing managedThing = thingService.save(thing);
		return thingMapper.toDTO(managedThing);
	}
	
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable Long id, @AuthenticationPrincipal User user){
		thingService.deleteById(id);
	}

}

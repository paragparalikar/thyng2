package com.thyng.web;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.domain.thing.Thing;
import com.thyng.domain.thing.ThingDTO;
import com.thyng.domain.thing.ThingDetailsDTO;
import com.thyng.domain.thing.ThingMapper;
import com.thyng.domain.thing.ThingService;
import com.thyng.domain.user.User;
import com.thyng.model.ThingStatus;
import com.thyng.model.mapper.ThingStatusMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/things")
public class ThingController {

	private final ThingMapper thingMapper;
	private final ThingService thingService;
	private final ThingStatusMapper thingStatusMapper;
	
	@GetMapping
	public Set<ThingDTO> findAll(@AuthenticationPrincipal User user){
		return thingMapper.dto(thingService.findByTenantId(user.getTenant().getId()));
	}
	
	@GetMapping("/{id}")
	public ThingDetailsDTO findById(@PathVariable @NotNull @Positive Long id){
		return thingMapper.dto(thingService.findById(id));
	}
	
	@GetMapping("/statuses")
	public Set<ThingStatus> getThingStatuses(@AuthenticationPrincipal User user){
		return thingStatusMapper.toThingStatuses(thingService.findByTenantId(user.getTenant().getId()));
	}
	
	@PostMapping
	@ResponseBody
	public ThingDetailsDTO create(@RequestBody @NotNull @Valid ThingDetailsDTO dto, @AuthenticationPrincipal User user){
		final Thing thing = thingMapper.entity(dto);
		thing.setTenant(user.getTenant());
		final Thing managedThing = thingService.create(thing);
		return thingMapper.dto(managedThing);
	}
	
	@PutMapping
	@ResponseBody
	public ThingDetailsDTO update(@RequestBody @NotNull @Valid ThingDetailsDTO dto){
		final Thing thing = thingMapper.entity(dto);
		final Thing managedThing = thingService.update(thing);
		return thingMapper.dto(managedThing);
	}
	
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable @NotNull @Positive final Long id){
		thingService.deleteById(id);
	}
	
	@GetMapping(params={"id", "name"})
	public String existsByIdNotAndNameAndTenantId(@RequestParam(defaultValue="0") Long id, @RequestParam String name, @AuthenticationPrincipal User user){
		return thingService.existsByIdNotAndNameAndTenantId(id, name, user.getTenant().getId()) ? "[\"This name is already taken\"]" : Boolean.TRUE.toString();
	}

}

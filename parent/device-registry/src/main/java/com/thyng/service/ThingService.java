package com.thyng.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.thyng.model.entity.Thing;
import com.thyng.model.exception.NotFoundException;
import com.thyng.repository.data.jpa.ThingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ThingService {

	private final ThingRepository thingRepository;
	
	public List<Thing> findByTenantId(Long tenantId){
		return thingRepository.findByTenantId(tenantId);
	}
	
	public Thing findById(Long id){
		final Thing thing = thingRepository.findById(id).orElseThrow(() -> new NotFoundException());
		thing.getProperties();
		thing.getMetrics();
		thing.getTenant();
		thing.getTags();
		return thing;
	}

	public Thing findByKey(String key){
		final Thing thing = thingRepository.findByKey(key).orElseThrow(() -> new NotFoundException());
		thing.getProperties();
		thing.getMetrics();
		thing.getTenant();
		thing.getTags();
		return thing;
	}
	
	public Thing save(Thing thing){
		return thingRepository.save(thing);
	}
	
	public void deleteById(Long id){
		thingRepository.deleteById(id);
	}
}

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
	
	public List<Thing> findAll(){
		return thingRepository.findAll();
	}
	
	public Thing findById(Long id){
		return thingRepository.findById(id).orElseThrow(() -> new NotFoundException());
	}

	public Thing findByKey(String key){
		return thingRepository.findByKey(key).orElseThrow(() -> new NotFoundException());
	}
	
	public Thing save(Thing thing){
		return thingRepository.save(thing);
	}
	
	public void deleteById(Long id){
		thingRepository.deleteById(id);
	}
}

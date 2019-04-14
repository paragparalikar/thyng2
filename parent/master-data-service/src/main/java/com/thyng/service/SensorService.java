package com.thyng.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.thyng.repository.data.jpa.SensorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SensorService {

	private final SensorRepository sensorRepository;
	private final ApplicationEventPublisher eventPublisher;
	
	

}

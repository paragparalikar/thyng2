package com.thyng.service;

import javax.transaction.Transactional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.thyng.repository.data.jpa.SensorRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class SensorService {

	private final SensorRepository sensorRepository;
	private final ApplicationEventPublisher eventPublisher;
	

}

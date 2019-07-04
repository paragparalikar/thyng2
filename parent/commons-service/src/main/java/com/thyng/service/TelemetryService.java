package com.thyng.service;

import org.springframework.stereotype.Service;

import com.thyng.model.Telemetry;
import com.thyng.repository.TelemetryRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class TelemetryService {

	private final TelemetryRepository telemetryRepository;
	
	public void save(Telemetry telemetry) {
		telemetryRepository.save(telemetry);
	}

}

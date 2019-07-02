package com.thyng.telemetry;

import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.model.Telemetry;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/telemetries")
public class TelemetryController {

	@PostMapping
	public void create(
			@RequestBody @NotNull final Telemetry telemetry, 
			@RequestParam @NotNull final Long sensorId, 
			@RequestParam @NotNull final String uuid) {
		System.out.println(String.join(" , ", sensorId.toString(), uuid, telemetry.toString()));
	}

}

package com.thyng.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.service.GatewayService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/gateways")
@RequiredArgsConstructor
public class GatewayController {

	private final GatewayService gatewayService;
	

}

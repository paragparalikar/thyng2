package com.thyng.web;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.entity.Gateway;
import com.thyng.mapper.GatewayMapper;
import com.thyng.model.dto.GatewayConfigurationDTO;
import com.thyng.model.dto.GatewayRegistrationDTO;
import com.thyng.service.GatewayService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/gateways")
@RequiredArgsConstructor
public class GatewayController {

	private final GatewayMapper gatewayMapper;
	private final GatewayService gatewayService;
	
	@PostMapping("/registrations")
	public GatewayConfigurationDTO register(@RequestBody GatewayRegistrationDTO dto, HttpServletRequest request){
		final String host = request.getRemoteHost();
		final Integer port = request.getRemotePort();
		final Gateway gateway = gatewayService.register(dto.getGatewayId(), host, port);
		return gatewayMapper.toExtendedDTO(gateway);
	}
	
	@GetMapping("/{id}/heartbeats") //For Head
	public void heartbeat(@PathVariable Long id){
		System.out.println("Heart beat received for id "+id);
	}
}
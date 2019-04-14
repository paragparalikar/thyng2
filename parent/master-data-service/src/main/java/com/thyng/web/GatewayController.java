package com.thyng.web;

import java.util.Set;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.model.dto.GatewayDTO;
import com.thyng.model.dto.GatewayDetailsDTO;
import com.thyng.model.dto.GatewayExtendedDetailsDTO;
import com.thyng.model.dto.GatewayRegistrationDTO;
import com.thyng.model.entity.Gateway;
import com.thyng.model.entity.User;
import com.thyng.model.mapper.GatewayMapper;
import com.thyng.service.GatewayService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/gateways")
@RequiredArgsConstructor
public class GatewayController {

	private final GatewayMapper gatewayMapper;
	private final GatewayService gatewayService;
	
	@GetMapping
	public Set<GatewayDTO> findAll(@AuthenticationPrincipal User user){
		return gatewayMapper.dto(gatewayService.findByTenantId(user.getTenant().getId()));
	}
	
	@GetMapping("/{id}")
	public GatewayDetailsDTO findOne(@PathVariable Long id){
		return gatewayMapper.toDetailsDTO(gatewayService.findById(id));
	}
	
	@PostMapping("/registrations")
	public GatewayExtendedDetailsDTO register(@RequestBody GatewayRegistrationDTO dto, HttpServletRequest request){
		final Gateway gateway = gatewayService.register(dto.getGatewayId(), request.getRemoteHost(), dto.getPort());
		return gatewayMapper.toExtendedDTO(gateway);
	}

	@GetMapping("/{id}/heartbeats") //For Head
	public void heartbeat(@PathVariable Long id){
		System.out.println("Heart beat received for id "+id);
	}
	
}

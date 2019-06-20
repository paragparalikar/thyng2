package com.thyng.web;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
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

import com.thyng.model.dto.GatewayConfigurationDTO;
import com.thyng.model.dto.GatewayDTO;
import com.thyng.model.dto.GatewayDetailsDTO;
import com.thyng.model.dto.GatewayRegistrationDTO;
import com.thyng.model.dto.GatewayThinDTO;
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
	
	@GetMapping(params="thin")
	public Set<GatewayThinDTO> findAllThin(@AuthenticationPrincipal User user){
		return gatewayMapper.thinDto(gatewayService.findThinByTenantId(user.getTenant().getId()));
	}
	
	@GetMapping("/{id}")
	public GatewayDetailsDTO findOne(@PathVariable @NotNull @Positive Long id){
		return gatewayMapper.toDetailsDTO(gatewayService.findById(id));
	}
	
	@PostMapping
	@ResponseBody
	public GatewayDetailsDTO create(@RequestBody @NotNull @Valid GatewayDetailsDTO dto, 
			@AuthenticationPrincipal User user){
		final Gateway gateway = gatewayMapper.entity(dto);
		gateway.setTenant(user.getTenant());
		final Gateway managedGateway = gatewayService.create(gateway);
		return gatewayMapper.toDetailsDTO(managedGateway);
	}
	
	@PutMapping
	@ResponseBody
	public GatewayDetailsDTO update(@RequestBody @NotNull @Valid GatewayDetailsDTO dto){
		final Gateway gateway = gatewayMapper.entity(dto);
		final Gateway managedGateway = gatewayService.update(gateway);
		return gatewayMapper.toDetailsDTO(managedGateway);
	}
	
	@DeleteMapping("/{id}")	
	public void delete(@PathVariable @NotNull @Positive Long id){
		gatewayService.deleteById(id);
	}
	
	@GetMapping(params={"id", "name"})
	public String existsByIdNotAndNameAndTenantId(@RequestParam(defaultValue="0") Long id, 
			@RequestParam String name, @AuthenticationPrincipal User user){
		return gatewayService.existsByIdNotAndNameAndTenantId(id, name, user.getTenant().getId()) ? 
				"[\"This name is already taken\"]" : Boolean.TRUE.toString();
	}
	
	
	@PostMapping("/registrations")
	public GatewayConfigurationDTO register(@RequestBody GatewayRegistrationDTO dto, HttpServletRequest request){
		final Gateway gateway = gatewayService.register(dto.getGatewayId(), request.getRemoteHost(), dto.getPort());
		return gatewayMapper.toExtendedDTO(gateway);
	}

	@GetMapping("/{id}/heartbeats") //For Head
	public void heartbeat(@PathVariable Long id){
		System.out.println("Heart beat received for id "+id);
	}
	
}

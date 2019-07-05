package com.thyng.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.thyng.entity.Actuator;
import com.thyng.entity.Gateway;
import com.thyng.entity.MqttClientConfig;
import com.thyng.entity.MqttLastWill;
import com.thyng.entity.Name;
import com.thyng.entity.Sensor;
import com.thyng.entity.Tenant;
import com.thyng.entity.Thing;
import com.thyng.entity.User;
import com.thyng.model.enumeration.Authority;
import com.thyng.model.enumeration.MqttQoS;
import com.thyng.repository.spring.data.jpa.ActuatorRepository;
import com.thyng.repository.spring.data.jpa.GatewayRepository;
import com.thyng.repository.spring.data.jpa.SensorRepository;
import com.thyng.repository.spring.data.jpa.TenantRepository;
import com.thyng.repository.spring.data.jpa.ThingRepository;
import com.thyng.repository.spring.data.jpa.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@Profile("dev")
@Scope("prototype")
@RequiredArgsConstructor
public class MockDataSetupService {

	private final UserRepository userRepository;
	
	private final TenantRepository tenantRepository;
	
	private final GatewayRepository gatewayRepository;
	
	private final ThingRepository thingRepository;
	
	private final SensorRepository sensorRepository;
	
	private final ActuatorRepository actuatorRepository;

	@Transactional
	public void setup(){
		for(int index = 0; index < 4; index++){
			final Tenant tenant = tenantRepository.save(buildTenant(index));
			log.info(tenant.toString());
			for(int userIndex = 0; userIndex < 10; userIndex++){
				userRepository.save(buildUser(tenant, userIndex, index));
			}
			for(int gatewayIndex = 0; gatewayIndex < 2; gatewayIndex++){
				final Gateway gateway = gatewayRepository.save(buildGateway(tenant, gatewayIndex));
				log.info(gateway.toString());
				for(int thingIndex = 0; thingIndex < 5; thingIndex++){
					final Thing thing = thingRepository.save(buildThing(tenant, gateway, thingIndex));
					log.info(thing.toString());
					sensorRepository.saveAll(buildSensors(thing));
					actuatorRepository.saveAll(buildActuators(thing));
				}
			}
		}
	}
	
	private Tenant buildTenant(int index){
		boolean locked = false;
		Calendar start = Calendar.getInstance();
		Calendar expiry = Calendar.getInstance();
		switch(index % 4){
		case 0 :
			start.add(Calendar.DATE, -1);
			expiry.add(Calendar.DATE, 1);
			break;
		case 1 : 
			start.add(Calendar.DATE, 1);
			expiry.add(Calendar.DATE, 1);
			break;
		case 2 : 
			start.add(Calendar.DATE, -1);
			break;
		case 3 : 
			start.add(Calendar.DATE, -1);
			expiry.add(Calendar.DATE, 1);
			locked = true;
			break;
		}
		
		return Tenant.builder()
				.id(null)
				.name("Tenant-"+index)
				.description("Description for Tenant-"+index)
				.properties(buildProperties())
				.start(start.getTime())
				.locked(locked)
				.expiry(expiry.getTime())
				.build();
	}
	
	private Gateway buildGateway(Tenant tenant, int index){
		return Gateway.builder()
				.id(null)
				.name("Gateway"+index+"-"+tenant.getName())
				.description("Description for Gateway")
				.tenant(tenant)
				.properties(buildProperties())
				.active(0 == (index % 2))
				.inactivityPeriod(60)
				.mqttClientConfig(buildMqttClientConfig())
				.build();
	}
	
	private MqttClientConfig buildMqttClientConfig(){
		return MqttClientConfig.builder()
				.id(null)
				.lastWill(buildMqttLastWill())
				.build();
	}
	
	private MqttLastWill buildMqttLastWill(){
		return MqttLastWill.builder()
				.topic("/thyng/abc")
				.message("Message")
				.qos(MqttQoS.EXACTLY_ONCE)
				.build();
	}
	
	private Thing buildThing(Tenant tenant, Gateway gateway, int thingIndex){
		return Thing.builder()
				.id(null)
				.tenant(tenant)
				.gateway(gateway)
				.active(0 == (thingIndex % 3))
				.description("Description for Thing-"+thingIndex)
				.name("Thing-"+thingIndex+"-"+gateway.getId())
				.properties(buildProperties())
				.build();
	}
	
	private Set<Sensor> buildSensors(Thing thing){
		final Set<Sensor> sensors = new HashSet<>();
		for(int index = 0; index < 4; index++){
			sensors.add(Sensor.builder()
					.id(null)
					.thing(thing)
					.abbreviation("ABB"+index)
					.description("Description for Sensor -"+index)
					.name("Sensor-"+index+"-"+thing.getName())
					.unit("Unit-"+index)
					.batchSize(10)
					.build());
		}
		return sensors;
	}
	
	public Set<Actuator> buildActuators(Thing thing){
		final Set<Actuator> actuators = new HashSet<>();
		for(int index = 0; index < 4; index++){
			actuators.add(Actuator.builder()
					.id(null)
					.thing(thing)
					.description("Description for Actuator -"+index)
					.name("Actuator-"+index+"-"+thing.getName())
					.abbreviation("ACT-"+index)
					.unit("Unit-"+index)
					.topic("/thyng/abc")
					.build());
		}
		return actuators;
	}
		
	private User buildUser(Tenant tenant, int userIndex, int tenantIndex){
		return User.builder()
				.id(null)
				.tenant(tenant)
				.name(Name.builder()
						.prefix("Mr.")
						.givenName("Fname-"+userIndex)
						.middleName("M")
						.familyName("Lname-"+userIndex)
						.build())
				.email(tenantIndex+"."+userIndex+"@gmail.com")
				.phone("+91-9960739342")
				.password("$2a$10$cmMk5hOSGKJ/96oQ5jXIv..FP7zCVUVKvi2Q28qS2tS08gvLghU42") // thyng
				.properties(buildProperties())
				.authorities(new HashSet<>(Arrays.asList(Authority.values())))
				.build();
	}
	
	private Map<String, String> buildProperties(){
		final Map<String, String> properties = new HashMap<>();
		properties.put("color", "red");
		properties.put("year", "2019");
		return properties;
	}
	
}

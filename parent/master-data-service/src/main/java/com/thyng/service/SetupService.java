package com.thyng.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thyng.model.entity.Actuator;
import com.thyng.model.entity.Gateway;
import com.thyng.model.entity.MqttClientConfig;
import com.thyng.model.entity.Name;
import com.thyng.model.entity.Sensor;
import com.thyng.model.entity.Tenant;
import com.thyng.model.entity.Thing;
import com.thyng.model.entity.User;
import com.thyng.model.enumeration.DataType;
import com.thyng.model.enumeration.MqttQoS;
import com.thyng.model.entity.MqttLastWill;
import com.thyng.repository.data.jpa.ActuatorRepository;
import com.thyng.repository.data.jpa.GatewayRepository;
import com.thyng.repository.data.jpa.SensorRepository;
import com.thyng.repository.data.jpa.TenantRepository;
import com.thyng.repository.data.jpa.ThingRepository;
import com.thyng.repository.data.jpa.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@Scope("prototype")
@RequiredArgsConstructor
public class SetupService {

	private final UserRepository userRepository;
	
	private final TenantRepository tenantRepository;
	
	private final GatewayRepository gatewayRepository;
	
	private final ThingRepository thingRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final SensorRepository sensorRepository;
	
	private final ActuatorRepository actuatorRepository;

	@Transactional
	public void setup(){
		for(int index = 0; index < 4; index++){
			final Tenant tenant = tenantRepository.save(buildTenant(index));
			for(int userIndex = 0; userIndex < 10; userIndex++){
				userRepository.save(buildUser(tenant, userIndex, index));
			}
			for(int gatewayIndex = 0; gatewayIndex < 2; gatewayIndex++){
				final Gateway gateway = gatewayRepository.save(buildGateway(tenant, gatewayIndex));
				System.out.println(gateway.getId());
				for(int thingIndex = 0; thingIndex < 5; thingIndex++){
					final Thing thing = thingRepository.save(buildThing(tenant, gateway, thingIndex));
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
				.tags(buildTags())
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
				.tags(buildTags())
				.properties(buildProperties())
				.alive(0 == (index % 2))
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
				.alive(0 == (thingIndex % 3))
				.description("Description for Thing-"+thingIndex)
				.name("Thing-"+thingIndex)
				.tags(buildTags())
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
					.dataType(DataType.values()[index % DataType.values().length])
					.description("Description for Sensor -"+index)
					.name("Sensor-"+index+"-"+thing.getName())
					.unit("Unit-"+index)
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
					.dataType(DataType.values()[index % DataType.values().length])
					.description("Description for Actuator -"+index)
					.name("Actuator-"+index+"-"+thing.getName())
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
				.password(passwordEncoder.encode("thyng"))
				.tags(buildTags())
				.properties(buildProperties())
				.build();
	}
	
	private Set<String> buildTags(){
		return new HashSet<>(Arrays.asList("tag1","tag2","tag3"));
	}
	
	private Map<String, String> buildProperties(){
		final Map<String, String> properties = new HashMap<>();
		properties.put("color", "red");
		properties.put("year", "2019");
		return properties;
	}
	
}

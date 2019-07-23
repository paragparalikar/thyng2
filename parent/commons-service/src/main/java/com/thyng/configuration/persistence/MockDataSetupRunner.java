package com.thyng.configuration.persistence;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.thyng.domain.actuator.Actuator;
import com.thyng.domain.actuator.ActuatorService;
import com.thyng.domain.gateway.Gateway;
import com.thyng.domain.gateway.GatewayService;
import com.thyng.domain.mqtt.MqttClientConfig;
import com.thyng.domain.mqtt.MqttLastWill;
import com.thyng.domain.mqtt.MqttQoS;
import com.thyng.domain.sensor.Sensor;
import com.thyng.domain.sensor.SensorService;
import com.thyng.domain.tenant.Tenant;
import com.thyng.domain.tenant.TenantService;
import com.thyng.domain.thing.Thing;
import com.thyng.domain.thing.ThingService;
import com.thyng.domain.user.Authority;
import com.thyng.domain.user.Name;
import com.thyng.domain.user.User;
import com.thyng.domain.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@Profile("dev")
@Scope("prototype")
@RequiredArgsConstructor
public class MockDataSetupRunner implements CommandLineRunner{

	private final UserService userRepository;
	
	private final TenantService tenantRepository;
	
	private final GatewayService gatewayRepository;
	
	private final ThingService thingRepository;
	
	private final SensorService sensorRepository;
	
	private final ActuatorService actuatorRepository;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		log.debug("-------------------------------------------------------------------------------");
		log.debug("                            Mock Data Setup                                    ");
		log.debug("-------------------------------------------------------------------------------");
		for(int index = 0; index < 4; index++){
			final Tenant tenant = tenantRepository.create(buildTenant(index));
			log.debug(tenant.toString());
			log.debug("Generating data for tenant with index "+index+" - "+tenant);
			for(int userIndex = 0; userIndex < 10; userIndex++){
				final User user = buildUser(tenant, userIndex, index);
				log.debug("Persisting " + user.toString());
				final User managedUser = userRepository.save(user);
				log.debug("Persisted " + managedUser.toString());
			}
			for(int gatewayIndex = 0; gatewayIndex < 2; gatewayIndex++){
				final Gateway gateway = gatewayRepository.create(buildGateway(tenant, gatewayIndex));
				log.debug(gateway.toString());
				for(int thingIndex = 0; thingIndex < 5; thingIndex++){
					final Thing thing = thingRepository.create(buildThing(tenant, gateway, thingIndex));
					buildSensors(thing).forEach(sensorRepository::create);
					buildActuators(thing).forEach(actuatorRepository::create);
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
					.inactivityPeriod(3)
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

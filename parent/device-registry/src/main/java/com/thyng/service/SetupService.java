package com.thyng.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thyng.model.entity.Metric;
import com.thyng.model.entity.Name;
import com.thyng.model.entity.Tenant;
import com.thyng.model.entity.Thing;
import com.thyng.model.entity.User;
import com.thyng.model.enumeration.DataType;
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
	
	private final ThingRepository thingRepository;
	
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void setup(){
		for(int index = 0; index < 3; index++){
			final Tenant tenant = tenantRepository.save(buildTenant(index));
			for(int userIndex = 0; userIndex < 10; userIndex++){
				userRepository.save(buildUser(tenant, userIndex, index));
			}
			for(int thingIndex = 0; thingIndex < 50; thingIndex++){
				thingRepository.save(buildThing(tenant, thingIndex));
			}
		}
	}
	
	private Tenant buildTenant(int index){
		return Tenant.builder()
				.id(null)
				.name("Tenant-"+index)
				.description("Description for Tenant-"+index)
				.build();
	}
	
	private Thing buildThing(Tenant tenant, int thingIndex){
		return Thing.builder()
				.id(null)
				.tenant(tenant)
				.alive(0 == (thingIndex % 3))
				.biDirectional(0 == (thingIndex % 2))
				.description("Description for Thing-"+thingIndex)
				.name("Thing-"+thingIndex)
				.metrics(buildMetrics())
				.tags(buildTags())
				.properties(buildProperties())
				.build();
	}
	
	private Set<Metric> buildMetrics(){
		final Set<Metric> metrics = new HashSet<>();
		for(int index = 0; index < 4; index++){
			metrics.add(Metric.builder()
					.id(null)
					.abbreviation("ABB"+index)
					.dataType(DataType.values()[index % DataType.values().length])
					.description("Description for Metric-"+index)
					.name("Metric-"+index)
					.unit("Unit-"+index)
					.build());
		}
		return metrics;
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

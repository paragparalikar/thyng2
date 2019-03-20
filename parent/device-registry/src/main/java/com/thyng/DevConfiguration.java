package com.thyng;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import com.thyng.model.entity.Metric;
import com.thyng.model.entity.Organisation;
import com.thyng.model.entity.Template;
import com.thyng.model.entity.Thing;
import com.thyng.model.enumeration.DataType;
import com.thyng.repository.data.jpa.OrganisationRepository;
import com.thyng.repository.data.jpa.ThingRepository;
import com.thyng.service.TemplateService;

@Profile("dev")
@Configuration
public class DevConfiguration{

	private Organisation organisation;
	private final Set<Thing> things = new HashSet<>();
	private final Set<Template> templates = new HashSet<>();
	
	private Set<String> buildTags(){
		return new HashSet<>(Arrays.asList("tag1","tag2","tag3"));
	}
	
	private Map<String, String> buildProperties(){
		final Map<String, String> properties = new HashMap<>();
		properties.put("color", "red");
		properties.put("year", "2019");
		return properties;
	}
	
	@Bean
	@Order(1)
	public CommandLineRunner createOrganisations(OrganisationRepository organisationRepository){
		return args -> {
			organisation = organisationRepository.save(new Organisation(null, "MyOrg"));
		};
	}
		
	@Bean
	@Order(3)
	public CommandLineRunner createTemplates(TemplateService templateService){
		return args -> {
			for(int index = 0; index < 25; index++){
				final Template template = Template.builder()
						.id(null)
						.name("Template-"+index)
						.organisation(organisation)
						.inactivityPeriod(60)
						.description("Description for template # "+index)
						.tags(buildTags())
						.properties(buildProperties())
						.build();
				template.setMetrics(buildMetrics(template));
				templates.add(template);
			}
			templates.forEach(templateService::save);
		};
	}
	
	private Set<Metric> buildMetrics(Template template){
		final Set<Metric> metrics = new HashSet<>();
		for(int index = 0; index < 4; index++){
			final Metric metric = Metric.builder()
					.template(template)
					.unit("unitA")
					.id(null)
					.abbreviation("UN")
					.dataType(DataType.NUMBER)
					.description("Description for Metric # "+index)
					.name("Metric-"+index)
					.build();
			metrics.add(metric);
		}
		return metrics;
	}
	
	@Bean
	@Order(5)
	public CommandLineRunner createThings(ThingRepository thingRepository){
		return args -> {
			for(Template template : templates){
				for(int index = 0; index < 12; index++){
					final Thing thing = Thing.builder()
							.id(null)
							.name("Thing-"+index)
							.organisation(organisation)
							.template(template)
							.description("Description for thing # "+index)
							.tags(buildTags())
							.properties(buildProperties())
							.build();
					things.add(thing);
				}
			}
			thingRepository.saveAll(things);
		};
	}
	
	
}

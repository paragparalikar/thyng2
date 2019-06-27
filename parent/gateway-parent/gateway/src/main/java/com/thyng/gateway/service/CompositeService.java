package com.thyng.gateway.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompositeService implements Service {

	private final Set<Service> services = new HashSet<>();

	public CompositeService(Service... services) {
		this.services.addAll(Arrays.asList(services));
	}
	
	public boolean add(Service service){
		return services.add(service);
	}
	
	public boolean remove(Service service){
		return services.remove(service);
	}

	@Override
	public void start() throws Exception {
		for (Service service : services) {
			if (null != service) {
				try {
					service.start();
				} catch (Exception e) {
					log.error(service.getClass().getSimpleName() + " - Init failed", e);
				}
			}
		}
	}

	@Override
	public void stop() throws Exception {
		for (Service service : services) {
			if (null != service) {
				try {
					service.stop();
				} catch (Exception e) {
					log.error(service.getClass().getSimpleName() + " - Destroy failed", e);
				}
			}
		}
	}

}

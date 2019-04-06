package com.thyng.gateway.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompositeService implements Service {

	private Service[] services;

	public CompositeService(Service... services) {
		this.services = services;
	}

	@Override
	public void start() throws Exception {
		if (null != services) {
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
	}

	@Override
	public void stop() throws Exception {
		if (null != services) {
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

}

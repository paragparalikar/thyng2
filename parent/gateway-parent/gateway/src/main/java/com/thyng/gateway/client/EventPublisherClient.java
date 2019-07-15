package com.thyng.gateway.client;

import com.thyng.gateway.EventBus;
import com.thyng.netty.Client;

import lombok.NonNull;

public class EventPublisherClient implements Client {
	public static final String ACTIVITY = "thyng-client-activity";

	private final Client delegate;
	private final EventBus eventBus;
	
	public EventPublisherClient(@NonNull final Client delegate, @NonNull final EventBus eventBus) {
		this.delegate = delegate;
		this.eventBus = eventBus;
	}

	@Override
	public <T> T execute(Object request) {
		final T response = delegate.execute(request);
		eventBus.publish(EventPublisherClient.ACTIVITY, System.currentTimeMillis());
		return response;
	}

}

package com.thyng.domain.actuator;

import lombok.NonNull;

public enum Protocol {

	MQTT("mqtt"), COAP("coap"), HTTP("http");

	private final String text;
	
	private Protocol(@NonNull final String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
}

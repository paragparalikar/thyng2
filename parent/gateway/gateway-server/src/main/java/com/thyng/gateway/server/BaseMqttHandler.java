package com.thyng.gateway.server;

import io.netty.buffer.ByteBuf;
import nl.jk5.mqtt.MqttHandler;

public class BaseMqttHandler implements MqttHandler {

	@Override
	public void onMessage(String topic, ByteBuf payload) {

	}

}

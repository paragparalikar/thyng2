package com.thyng.model.enumeration;

import lombok.Getter;

@Getter
public enum MqttVersion {

	MQTT_3_1("MQIsdp", (byte) 3),
    MQTT_3_1_1("MQTT", (byte) 4);

    private final String name;
    private final byte level;

    MqttVersion(String protocolName, byte protocolLevel) {
        name = protocolName;
        level = protocolLevel;
    }

}

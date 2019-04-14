package com.thyng.model;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.thyng.model.dto.ActuatorDTO;
import com.thyng.model.dto.GatewayDTO;
import com.thyng.model.dto.GatewayExtendedDetailsDTO;
import com.thyng.model.dto.GatewayRegistrationDTO;
import com.thyng.model.dto.MqttClientConfigDTO;
import com.thyng.model.dto.MqttLastWillDTO;
import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.ThingDTO;
import com.thyng.model.dto.ThingDetailsDTO;
import com.thyng.model.enumeration.DataType;
import com.thyng.model.enumeration.GatewayOps;
import com.thyng.model.enumeration.MqttQoS;
import com.thyng.model.enumeration.MqttVersion;
import com.thyng.model.enumeration.Protocol;

public class Serializer {

	private static Kryo kryo;
	public static Kryo kryo(){
		if(null == kryo){
			kryo = new Kryo();
			kryo.register(ActuatorDTO.class);
			kryo.register(GatewayExtendedDetailsDTO.class);
			kryo.register(GatewayDTO.class);
			kryo.register(GatewayRegistrationDTO.class);
			kryo.register(MqttClientConfigDTO.class);
			kryo.register(MqttLastWillDTO.class);
			kryo.register(SensorDTO.class);
			kryo.register(ThingDetailsDTO.class);
			kryo.register(ThingDTO.class);
			kryo.register(DataType.class);
			kryo.register(GatewayOps.class);
			kryo.register(MqttQoS.class);
			kryo.register(MqttVersion.class);
			kryo.register(Protocol.class);
			
			kryo.register(HashMap.class);
			kryo.register(HashSet.class);
		}
		return kryo;
	}
	
	
	public static byte[] write(Object object){
		Objects.requireNonNull(object);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final Output output = new Output(baos);
		Serializer.kryo().writeObject(output, object);
		output.flush();
		return baos.toByteArray();
	}
}

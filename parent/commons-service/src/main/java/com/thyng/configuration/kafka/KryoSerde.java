package com.thyng.configuration.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.thyng.domain.actuator.ActuatorDTO;
import com.thyng.domain.actuator.Protocol;
import com.thyng.domain.gateway.GatewayConfigurationDTO;
import com.thyng.domain.gateway.GatewayDTO;
import com.thyng.domain.gateway.GatewayDetailsDTO;
import com.thyng.domain.gateway.GatewayThinDTO;
import com.thyng.domain.mqtt.MqttClientConfigDTO;
import com.thyng.domain.mqtt.MqttLastWillDTO;
import com.thyng.domain.mqtt.MqttQoS;
import com.thyng.domain.mqtt.MqttVersion;
import com.thyng.domain.sensor.SensorDTO;
import com.thyng.domain.tenant.TenantDTO;
import com.thyng.domain.thing.ThingDTO;
import com.thyng.domain.thing.ThingDetailsDTO;
import com.thyng.domain.user.Authority;
import com.thyng.domain.user.UserDTO;

public class KryoSerde<T> implements Serializer<T>, Deserializer<T>, Serde<T>{
	
	private static final Kryo KRYO = new Kryo();
	public static final KryoSerde<?> INSTANCE = new KryoSerde<>();
	
	static {
		KRYO.register(byte[].class, 10);
		KRYO.register(long[].class, 11);
		KRYO.register(byte[][].class, 12);
		KRYO.register(long[][].class, 13);

		KRYO.register(HashMap.class, 20);
		KRYO.register(HashSet.class, 21);
		KRYO.register(ArrayList.class, 22);
		
		KRYO.register(Authority.class, 50);
		KRYO.register(MqttQoS.class, 51);
		KRYO.register(MqttVersion.class, 52);
		KRYO.register(Protocol.class, 53);
		KRYO.register(ActuatorDTO.class, 54);
		KRYO.register(GatewayConfigurationDTO.class, 55);
		KRYO.register(GatewayDetailsDTO.class, 56);
		KRYO.register(GatewayDTO.class, 57);
		KRYO.register(GatewayThinDTO.class, 58);
		KRYO.register(MqttClientConfigDTO.class, 59);
		KRYO.register(MqttLastWillDTO.class, 60);
		KRYO.register(SensorDTO.class, 61);
		KRYO.register(TenantDTO.class, 62);
		KRYO.register(ThingDetailsDTO.class, 63);
		KRYO.register(ThingDTO.class, 64);
		KRYO.register(UserDTO.class, 65);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> KryoSerde<T> instance(){
		return (KryoSerde<T>) INSTANCE;
	}

	@Override
	public Serializer<T> serializer() {
		return this;
	}

	@Override
	public Deserializer<T> deserializer() {
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T deserialize(String topic, byte[] data) {
		final Input input = new Input(data);
		return (T) KRYO.readClassAndObject(input);
	}

	@Override
	public byte[] serialize(String topic, T data) {
		return serialize(data, 4096);
	}
	
	private static byte[] serialize(Object object, int size) {
		try {
			final Output output = new Output(size);
			KRYO.writeClassAndObject(output, object);
			return output.toBytes();
		}catch(KryoException e) {
			if(size < 10240000) {
				return serialize(object, size*2);
			}else {
				throw e;
			}
		}
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		
	}

	@Override
	public void close() {
		
	}
	
}

package com.thyng;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.thyng.gateway.model.CommitRequest;
import com.thyng.gateway.model.CommitResponse;
import com.thyng.gateway.model.RollbackRequest;
import com.thyng.gateway.model.RollbackResponse;
import com.thyng.model.HeartbeatRequest;
import com.thyng.model.HeartbeatResponse;
import com.thyng.model.RegistrationRequest;
import com.thyng.model.RegistrationResponse;
import com.thyng.model.SensorStatusRequest;
import com.thyng.model.SensorStatusResponse;
import com.thyng.model.Telemetry;
import com.thyng.model.TelemetryRequest;
import com.thyng.model.TelemetryResponse;
import com.thyng.model.ThingStatusRequest;
import com.thyng.model.ThingStatusResponse;
import com.thyng.model.dto.ActuatorDTO;
import com.thyng.model.dto.GatewayConfigurationDTO;
import com.thyng.model.dto.GatewayDTO;
import com.thyng.model.dto.GatewayDetailsDTO;
import com.thyng.model.dto.GatewayThinDTO;
import com.thyng.model.dto.MqttClientConfigDTO;
import com.thyng.model.dto.MqttLastWillDTO;
import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.TenantDTO;
import com.thyng.model.dto.ThingDTO;
import com.thyng.model.dto.ThingDetailsDTO;
import com.thyng.model.dto.UserDTO;
import com.thyng.model.enumeration.Authority;
import com.thyng.model.enumeration.MqttQoS;
import com.thyng.model.enumeration.MqttVersion;
import com.thyng.model.enumeration.Protocol;

public class KryoSerializer implements Serializer {

	public static final KryoSerializer INSTANCE = new KryoSerializer();
	private static final Kryo KRYO = new Kryo();

	static {
		KRYO.register(byte[].class, 10);
		KRYO.register(long[].class, 11);
		KRYO.register(byte[][].class, 12);
		
		KRYO.register(Authority.class, 20);
		KRYO.register(MqttQoS.class, 21);
		KRYO.register(MqttVersion.class, 22);
		KRYO.register(Protocol.class, 23);
		KRYO.register(HashMap.class, 24);
		KRYO.register(HashSet.class, 25);
		KRYO.register(ArrayList.class, 26);
		KRYO.register(ActuatorDTO.class, 27);
		KRYO.register(GatewayConfigurationDTO.class, 28);
		KRYO.register(GatewayDetailsDTO.class, 29);
		KRYO.register(GatewayDTO.class, 30);
		KRYO.register(GatewayThinDTO.class, 31);
		KRYO.register(MqttClientConfigDTO.class, 32);
		KRYO.register(MqttLastWillDTO.class, 33);
		KRYO.register(SensorDTO.class, 34);
		KRYO.register(TenantDTO.class, 35);
		KRYO.register(ThingDetailsDTO.class, 36);
		KRYO.register(ThingDTO.class, 37);
		KRYO.register(UserDTO.class, 38);
		KRYO.register(CommitRequest.class, 39);
		KRYO.register(CommitResponse.class, 40);
		KRYO.register(RollbackRequest.class, 41);
		KRYO.register(RollbackResponse.class, 42);
		KRYO.register(RegistrationRequest.class, 43);
		KRYO.register(RegistrationResponse.class, 44);
		KRYO.register(HeartbeatRequest.class, 45);
		KRYO.register(HeartbeatResponse.class, 46);
		KRYO.register(ThingStatusRequest.class, 47);
		KRYO.register(ThingStatusResponse.class, 48);
		KRYO.register(SensorStatusRequest.class, 49);
		KRYO.register(SensorStatusResponse.class, 50);
		KRYO.register(Telemetry.class, 51);
		KRYO.register(TelemetryRequest.class, 52);
		KRYO.register(TelemetryResponse.class, 53);
	}

	private KryoSerializer() {
	}
	
	@Override
	public byte[] serialize(Object object) {
		return serialize(object, 4096);
	}
	
	private byte[] serialize(Object object, int size) {
		try {
			final Output output = new Output(size);
			KRYO.writeClassAndObject(output, object);
			return output.toBytes();
		}catch(KryoException e) {
			if(size < 10240000) {
				return serialize(object, size*2);
			}
			throw e;
		}
	}
	
	@Override
	public void serialize(Object object, OutputStream outputStream) throws IOException {
		final Output output = new Output(outputStream);
		KRYO.writeClassAndObject(output, object);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T deserialize(final InputStream inputStream) {
		final Input input = new Input(inputStream);
		return (T) KRYO.readClassAndObject(input);
	}

}

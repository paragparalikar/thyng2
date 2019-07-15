package com.thyng;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.esotericsoftware.kryo.Kryo;
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
		KRYO.register(Authority.class);
		KRYO.register(MqttQoS.class);
		KRYO.register(MqttVersion.class);
		KRYO.register(Protocol.class);
		KRYO.register(Telemetry.class);
		KRYO.register(HashMap.class);
		KRYO.register(HashSet.class);
		KRYO.register(ArrayList.class);
		KRYO.register(ActuatorDTO.class);
		KRYO.register(GatewayConfigurationDTO.class);
		KRYO.register(GatewayDetailsDTO.class);
		KRYO.register(GatewayDTO.class);
		KRYO.register(GatewayThinDTO.class);
		KRYO.register(MqttClientConfigDTO.class);
		KRYO.register(MqttLastWillDTO.class);
		KRYO.register(SensorDTO.class);
		KRYO.register(TenantDTO.class);
		KRYO.register(ThingDetailsDTO.class);
		KRYO.register(ThingDTO.class);
		KRYO.register(UserDTO.class);
		KRYO.register(CommitRequest.class);
		KRYO.register(CommitResponse.class);
		KRYO.register(RollbackRequest.class);
		KRYO.register(RollbackResponse.class);
		KRYO.register(RegistrationRequest.class);
		KRYO.register(RegistrationResponse.class);
		KRYO.register(TelemetryRequest.class);
		KRYO.register(TelemetryResponse.class);
		KRYO.register(HeartbeatRequest.class);
		KRYO.register(HeartbeatResponse.class);
		KRYO.register(ThingStatusRequest.class);
		KRYO.register(ThingStatusResponse.class);
		KRYO.register(SensorStatusRequest.class);
		KRYO.register(SensorStatusResponse.class);
	}

	private KryoSerializer() {
	}
	
	@Override
	public byte[] serialize(Object object) {
		final Output output = new Output(4096);
		KRYO.writeClassAndObject(output, object);
		return output.toBytes();
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

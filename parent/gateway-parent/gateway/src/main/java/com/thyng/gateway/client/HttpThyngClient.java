package com.thyng.gateway.client;

import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.gateway.provider.serialization.SerializationProvider;
import com.thyng.model.Telemetry;
import com.thyng.model.dto.GatewayConfigurationDTO;
import com.thyng.model.dto.GatewayRegistrationDTO;

import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;
import lombok.NonNull;

public class HttpThyngClient implements ThyngClient, ObjectMapper {

	private final PropertyProvider properties;
	private final SerializationProvider<String> serializationProvider;

	public HttpThyngClient(@NonNull final PropertyProvider properties, 
			@NonNull final SerializationProvider<String> serializationProvider) {
		super();
		this.properties = properties;
		this.serializationProvider = serializationProvider;
		final String username = properties.get("thyng.server.username", "0.0@gmail.com");
		final String password = properties.get("thyng.server.password", "thyng");
		Unirest.config()
			.setObjectMapper(this)
			.setDefaultBasicAuth(username, password);
	}

	@Override
	public GatewayConfigurationDTO registerAndFetchDetails() throws Exception {
		final Long gatewayId = properties.getLong("thyng.gateway.id", null);
		final Integer port = properties.getInteger("thyng.gateway.http.server.port", null); 
		final String url = properties.get("thyng.server.url", null) + properties.get("thyng.server.url.registration",null);
		return Unirest
			.post(url)
			.body(GatewayRegistrationDTO.builder()
				.gatewayId(gatewayId)
				.port(port)
				.build())
			.asObject(GatewayConfigurationDTO.class)
			.getBody();
	}

	@Override
	public void heartbeat() throws Exception {
		final Long gatewayId = properties.getLong("thyng.gateway.id", null);
		final String url = properties.get("thyng.server.url", null) + properties.get("thyng.server.url.heartbeat",null);
		Unirest.head(url).routeParam("id", gatewayId.toString()).asEmpty();
	}

	@Override
	public void send(@NonNull final Telemetry telemetry) throws Exception {
		final String url = properties.get("thyng.server.url", null) + properties.get("thyng.server.url.telemetry",null);
		Unirest
			.post(url)
			.body(telemetry.toString())
			.queryString("sensorId", telemetry.getSensorId())
			.queryString("uuid", telemetry.getUuid())
			.asEmpty();
	}

	@Override
	public void sendThingStatus(@NonNull final Long thingId, @NonNull final Boolean active) throws Exception {
		
	}

	@Override
	public void sendSensorStatus(@NonNull final Long sensorId, @NonNull final Boolean active) throws Exception {

	}

	@Override
	public <T> T readValue(String value, Class<T> valueType) {
		return serializationProvider.deserialize(value, valueType);
	}

	@Override
	public String writeValue(Object value) {
		return serializationProvider.serialize(value);
	}

}

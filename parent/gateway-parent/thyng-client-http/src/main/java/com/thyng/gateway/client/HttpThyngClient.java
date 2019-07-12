package com.thyng.gateway.client;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.model.Telemetry;
import com.thyng.model.dto.GatewayConfigurationDTO;

import kong.unirest.ContentType;
import kong.unirest.HeaderNames;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpThyngClient implements ThyngClient, ObjectMapper {

	private final Gson gson = new Gson();
	private final PropertyProvider properties;

	public HttpThyngClient(@NonNull final PropertyProvider properties) {
		super();
		this.properties = properties;
		final String username = properties.get("thyng.server.username", "0.0@gmail.com");
		final String password = properties.get("thyng.server.password", "thyng");
		Unirest.config()
			.addShutdownHook(true)
			.setObjectMapper(this)
			.setDefaultHeader(HeaderNames.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
			.setDefaultBasicAuth(username, password);
	}

	/**
	 * As we do not want connection to this route pooled, lets not use http client at all.
	 */
	@Override
	public GatewayConfigurationDTO registerAndFetchDetails() throws Exception {
		final Long gatewayId = properties.getLong("thyng.gateway.id", null);
		final String url = properties.get("thyng.server.url", null) + properties.get("thyng.server.url.registration",null)
			.replaceAll("\\{id\\}", gatewayId.toString());
	
		log.info("Using raw http connection to fetch details");
		final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.addRequestProperty(HeaderNames.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		connection.connect();
		final int responseCode = connection.getResponseCode();
		if(200 <= responseCode && 300 > responseCode) {
			final GatewayConfigurationDTO dto = gson.fromJson(new InputStreamReader(connection.getInputStream()), GatewayConfigurationDTO.class);
			connection.disconnect();
			return dto;
		}else {
			connection.disconnect();
			throw new Exception("Code : " + responseCode + ", Message : " + connection.getResponseMessage());
		}
		
		/*
		 * return Unirest .get(url) .routeParam("id", gatewayId.toString())
		 * .asObject(GatewayConfigurationDTO.class) .ifFailure(response -> { throw new
		 * RuntimeException(response.getStatus()+" : "+response.getStatusText()); })
		 * .getBody();
		 */
	}

	
	@Override
	public void heartbeat() throws Exception {
		final Long gatewayId = properties.getLong("thyng.gateway.id", null);
		final String url = properties.get("thyng.server.url", null) + properties.get("thyng.server.url.heartbeat",null);
		Unirest.head(url).routeParam("id", gatewayId.toString()).asString().ifFailure(response -> {
			throw new RuntimeException(response.getStatus()+" : "+response.getStatusText());
		});
	}

	@Override
	public void send(@NonNull final Telemetry telemetry) throws Exception {
		final String url = properties.get("thyng.server.url", null) + properties.get("thyng.server.url.telemetry",null);
		Unirest
			.post(url)
			.body(telemetry.toString())
			.queryString("sensorId", telemetry.getSensorId())
			.queryString("uuid", telemetry.getUuid())
			.asString()
			.ifFailure(response -> {
				throw new RuntimeException(response.getStatus()+" : "+response.getStatusText());
			});
	}

	@Override
	public void sendThingStatus(@NonNull final Long thingId, @NonNull final Boolean active) throws Exception {
		
	}

	@Override
	public void sendSensorStatus(@NonNull final Long sensorId, @NonNull final Boolean active) throws Exception {

	}

	@Override
	@SneakyThrows
	public <T> T readValue(String value, Class<T> valueType) {
		return gson.fromJson(value, valueType);
	}

	@Override
	@SneakyThrows
	public String writeValue(Object value) {
		return gson.toJson(value);
	}

}

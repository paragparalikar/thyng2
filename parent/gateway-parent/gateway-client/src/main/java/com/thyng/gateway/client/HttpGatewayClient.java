package com.thyng.gateway.client;

import com.google.gson.Gson;
import com.thyng.model.dto.GatewayConfigurationDTO;

import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
public class HttpGatewayClient implements GatewayClient, ObjectMapper {
	private static final Gson GSON = new Gson();
	
	private final String baseUrl;
	private final GatewayConfigurationDTO details;

	@Builder
	public HttpGatewayClient(final GatewayConfigurationDTO details) {
		this.details = details;
		baseUrl = (details.getSsl() ? "https" : "http")+"://"+details.getHost()+":"+details.getPort();
		if(details.getSsl()) {
			//Unirest.config().clientCertificateStore(store, password);
		}
	}
	
	@Override
	public void commit(@NonNull final String uuid) {
		Unirest
			.delete(baseUrl+"/telemetries")
			.queryString("uuid", uuid)
			.asEmpty();
	}
	
	@Override
	public void rollback(@NonNull final String uuid) {
		Unirest
		.put(baseUrl+"/telemetries")
		.queryString("uuid", uuid)
		.asEmpty();
	}

	@Override
	public <T> T readValue(String value, Class<T> valueType) {
		return GSON.fromJson(value, valueType);
	}

	@Override
	public String writeValue(Object value) {
		return GSON.toJson(value);
	}
	
}

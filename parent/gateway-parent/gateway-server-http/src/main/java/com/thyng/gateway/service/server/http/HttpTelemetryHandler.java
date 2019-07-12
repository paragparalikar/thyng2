package com.thyng.gateway.service.server.http;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thyng.gateway.Constant;
import com.thyng.gateway.model.Context;
import com.thyng.gateway.provider.persistence.TelemetryStore;
import com.thyng.gateway.telemetry.TelemetryMessageHandler;
import com.thyng.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpTelemetryHandler implements HttpHandler {

	private final Context context;
	private final TelemetryMessageHandler telemetryMessageHandler;

	public HttpTelemetryHandler(final Context context) {
		this.context = context;
		telemetryMessageHandler = new TelemetryMessageHandler(context);
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try{
			switch(exchange.getRequestMethod()) {
			case "POST": post(exchange); break;
			case "DELETE": delete(exchange); break;
			case "PUT": put(exchange); break;
			case "GET": break;
			}
			exchange.sendResponseHeaders(200, 0);
		}catch(Throwable throwable) {
			log.error("Failed to process telemetry", throwable);
			exchange.sendResponseHeaders(500, 0);
		}finally {
			exchange.close();
		}
	}
	
	private void post(HttpExchange exchange) throws IOException {
		final String payload = StringUtil.convert(exchange.getRequestBody(), Constant.CHARSET);
		telemetryMessageHandler.handle(payload);
	}
	
	private void delete(HttpExchange exchange) {
		doWithTelemetryStore(exchange, (telemetryStore, uuid) -> telemetryStore.commit(uuid));
	}
	
	private void put(HttpExchange exchange) {
		doWithTelemetryStore(exchange, (telemetryStore, uuid) -> telemetryStore.rollback(uuid));
	}

	private void doWithTelemetryStore(HttpExchange exchange, BiConsumer<TelemetryStore, String> consumer) {
		final String query = exchange.getRequestURI().getQuery();
		final Map<String, List<String>> parameters = StringUtil.splitQuery(query);
		final Long sensorId = Long.parseLong(parameters.get("sensorId").get(0));
		final String uuid = parameters.get("uuid").get(0);
		final TelemetryStore telemetryStore = context.getPersistenceProvider().getTelemetryStore(sensorId);
		consumer.accept(telemetryStore, uuid);
	}
	
}

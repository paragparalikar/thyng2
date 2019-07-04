package com.thyng.gateway.service.server.http;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import com.thyng.gateway.model.Context;
import com.thyng.gateway.service.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class HttpServerService implements Service {

	private HttpServer httpServer;
	private final Context context;

	@Override
	public void start() throws Exception {
		final int port = context.getProperties().getInteger("thyng.gateway.http.server.port", 80);
		final int backlog = context.getProperties().getInteger("thyng.gateway.http.server.backlog", 0);
		log.info("Starting HTTP server at port "+port+" and backlog "+backlog);
		httpServer = HttpServer.create(new InetSocketAddress(port), backlog);
		httpServer.createContext("/telemetries", new HttpTelemetryHandler(context));
		log.info("HTTT server started successfully");
	}

	@Override
	public void stop() throws Exception {
		final int delay = context.getProperties().getInteger("thyng.gateway.http.server.stop.delay", 3);
		log.info("Stopping HTTP server with delay "+delay);
		httpServer.stop(delay);
		log.info("HTTT server stopped successfully");
	}

}

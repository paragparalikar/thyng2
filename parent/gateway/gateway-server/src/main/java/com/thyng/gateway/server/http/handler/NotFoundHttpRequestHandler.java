package com.thyng.gateway.server.http.handler;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class NotFoundHttpRequestHandler extends HttpRequestHandler {

	@Override
	protected FullHttpResponse handleRequest(FullHttpRequest request) throws Exception {
		return erroResponse(HttpResponseStatus.NOT_FOUND);
	}

}

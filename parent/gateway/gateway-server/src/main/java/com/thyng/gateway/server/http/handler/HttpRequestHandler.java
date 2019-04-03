package com.thyng.gateway.server.http.handler;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;

public abstract class HttpRequestHandler {

	public void handleRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		final Future<FullHttpResponse> responseFuture = handleRequest(ctx.channel().eventLoop(), request);
		responseFuture.addListener(future -> {
			final FullHttpResponse response = (FullHttpResponse) future.getNow();
			handleHttpHeaders(request, response);
			ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		});
	}

	protected Future<FullHttpResponse> handleRequest(EventExecutor executor, FullHttpRequest request){
		return executor.submit(() -> handleRequest(request));
	}
	
	protected abstract FullHttpResponse handleRequest(FullHttpRequest request) throws Exception;

	protected void handleHttpHeaders(FullHttpRequest request, FullHttpResponse response) {
		if (HttpUtil.isKeepAlive(request)) {
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
	}

	protected FullHttpResponse erroResponse(HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
				Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
		return response;
	}

}

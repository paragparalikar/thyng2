package com.thyng.gateway.server.http;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.Objects;
import java.util.regex.Pattern;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.CharsetUtil;

public abstract class HttpRequestUriChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private final Pattern pattern;

	public HttpRequestUriChannelHandler(final String uri) {
		Objects.requireNonNull(uri);
		this.pattern = Pattern.compile(uri);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if (!request.decoderResult().isSuccess()) {
			sendError(ctx, BAD_REQUEST);
			return;
		}else if(pattern.matcher(request.uri()).matches()){
			handleRequest(ctx, request);
		}else{
			ctx.fireChannelRead(request.retain());
		}
	}

	protected abstract void handleRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception;
	
	protected void handleHttpHeaders(FullHttpRequest request, FullHttpResponse response){
		if (HttpUtil.isKeepAlive(request)) {
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
	}
	
	protected void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
				Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

}

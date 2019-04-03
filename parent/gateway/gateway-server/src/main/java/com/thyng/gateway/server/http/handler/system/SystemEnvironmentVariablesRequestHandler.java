package com.thyng.gateway.server.http.handler.system;

import com.thyng.gateway.server.http.handler.HttpRequestHandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

@Sharable
public class SystemEnvironmentVariablesRequestHandler extends HttpRequestHandler {
	
	@Override
	protected FullHttpResponse handleRequest(FullHttpRequest request) throws Exception{
		final StringBuilder builder = new StringBuilder();
		System.getenv().forEach((key, value) -> builder.append("<div>"+key+" = "+value+"</div>"));
		final DefaultFullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, 
				HttpResponseStatus.OK, 
				Unpooled.copiedBuffer(
						builder.toString(), 
						CharsetUtil.UTF_8));
		response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=utf-8");
		return response;
	}
	
}

package com.thyng.gateway.server.http;

import java.util.Objects;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

@Sharable
public class SystemPropertiesRequestHandler extends HttpRequestUriChannelHandler{

	public SystemPropertiesRequestHandler(final String uri) {
		super(uri);
	}

	@Override
	protected void handleRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if(Objects.equals(HttpMethod.GET, request.method())){
			handleGetSystemProperties(ctx, request);
		}
	}
	
	private void handleGetSystemProperties(ChannelHandlerContext ctx, FullHttpRequest request){
		final StringBuilder builder = new StringBuilder();
		System.getProperties().forEach((key, value) -> builder.append("<div>"+key+" = "+value+"</div>"));
		final DefaultFullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, 
				HttpResponseStatus.OK, 
				Unpooled.copiedBuffer(
						builder.toString(), 
						CharsetUtil.UTF_8));
		response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=utf-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
	
}

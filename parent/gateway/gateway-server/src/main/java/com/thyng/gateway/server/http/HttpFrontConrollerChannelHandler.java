package com.thyng.gateway.server.http;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import com.thyng.gateway.server.http.handler.HttpRequestHandler;
import com.thyng.gateway.server.http.handler.NotFoundHttpRequestHandler;
import com.thyng.gateway.server.http.handler.system.SystemEnvironmentVariablesRequestHandler;
import com.thyng.gateway.server.http.handler.system.SystemMXBeanInfoRequestHandler;
import com.thyng.gateway.server.http.handler.system.SystemPropertiesRequestHandler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

@Sharable
public class HttpFrontConrollerChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

	private final HashMultimap<String, RequestHandlerMapping> requestHandlers = HashMultimap.create();
	private final NotFoundRequestUriHandlerMapping notFoundHandlerMapping = new NotFoundRequestUriHandlerMapping();
	
	public HttpFrontConrollerChannelHandler() {
		cache(new RequestUriHandlerMapping("/system/environmental-variables", new SystemEnvironmentVariablesRequestHandler(), HttpMethod.GET));
		cache(new RequestUriHandlerMapping("/system/properties", new SystemPropertiesRequestHandler(), HttpMethod.GET));
		cache(new RequestUriHandlerMapping("/system/mx", new SystemMXBeanInfoRequestHandler(), HttpMethod.GET));
	}
	
	private void cache(RequestUriHandlerMapping mapping){
		requestHandlers.put(mapping.uri(), mapping);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		requestHandlers
			.get(request.uri()).stream()
			.filter(handler -> handler.matches(request))
			.findFirst().orElse(notFoundHandlerMapping)
			.handler().handleRequest(ctx, request);
	}
	
}

abstract class RequestHandlerMapping{
	private final String uri;
	private final HttpRequestHandler handler; 
	
	public RequestHandlerMapping(String uri, HttpRequestHandler handler) {
		this.uri = uri;
		this.handler = handler;
	}
	
	public String uri(){
		return uri;
	}
	
	public HttpRequestHandler handler(){
		return handler;
	}
	
	public abstract boolean matches(HttpRequest request);
	
}

class RequestUriHandlerMapping extends RequestHandlerMapping{
	private final HttpMethod httpMethod;
	private final Predicate<String> predicate;
	
	public RequestUriHandlerMapping(String uri, HttpRequestHandler handler, HttpMethod httpMethod) {
		super(uri, handler);
		this.httpMethod = httpMethod;
		this.predicate = null == uri ? null : Pattern.compile(uri).asPredicate();
	}

	@Override
	public boolean matches(HttpRequest request){
		return predicate.test(request.uri()) 
				&& Objects.equal(httpMethod, request.method());
	}
	
}

class NotFoundRequestUriHandlerMapping extends RequestHandlerMapping{

	public NotFoundRequestUriHandlerMapping() {
		super(null, new NotFoundHttpRequestHandler());
	}
	
	@Override
	public boolean matches(HttpRequest request) {
		return true;
	}
	
}
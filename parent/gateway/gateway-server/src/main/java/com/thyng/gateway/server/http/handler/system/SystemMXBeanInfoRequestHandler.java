package com.thyng.gateway.server.http.handler.system;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

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
public class SystemMXBeanInfoRequestHandler extends HttpRequestHandler {

	private List<String> excludeMethods = Arrays.asList("getLastGcInfo","getMemoryManagers", "getMemoryManagers0");
	
	@Override
	@SuppressWarnings("unchecked")
	protected FullHttpResponse handleRequest(FullHttpRequest request) throws Exception {
		final StringBuilder builder = new StringBuilder();
		final Map<Object,Object> references = new IdentityHashMap<>();
		final Method[] methods = ManagementFactory.class.getDeclaredMethods();
		for(Method method : methods){
			if(method.getName().startsWith("get") && 0 == method.getParameters().length){
				method.setAccessible(true);
				if(method.getName().endsWith("Bean")){
					final Object bean = method.invoke(null);
					checkThenAppendOrRecurse(bean, method, builder, references);
				}else if(method.getName().endsWith("Beans")){
					final Iterable<Object> beans = (Iterable<Object>) method.invoke(null);
					for(Object bean : beans){
						checkThenAppendOrRecurse(bean, method, builder, references);
					}
				}
			}
		}
		
		final DefaultFullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, 
				HttpResponseStatus.OK, 
				Unpooled.copiedBuffer(
						builder.toString(), 
						CharsetUtil.UTF_8));
		response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=utf-8");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	private void build(Object bean, StringBuilder builder, final Map<Object, Object> references){
		try{
			builder.append("<br><h3>"+bean.getClass().getSimpleName()+"</h3>");
			final Method[] methods = bean.getClass().getDeclaredMethods();
			for(Method method : methods){
				method.setAccessible(true);
				if((method.getName().startsWith("get") || method.getName().startsWith("is")) && 
						Modifier.isPublic(method.getModifiers()) &&
						!Modifier.isStatic(method.getModifiers()) &&
						0 == method.getParameters().length &&
						!excludeMethods.contains(method.getName())){
					final Object result = method.invoke(bean);
					if(!references.containsKey(result)){
						if(result.getClass().isArray()){
							for(int index = 0; index < Array.getLength(result); index++){
								final Object value = Array.get(result, index);
								checkThenAppendOrRecurse(value, method, builder, references);
							}
						}else if(result instanceof Iterable){
							final Iterable<Object> values = (Iterable<Object>)result;
							for(Object value : values){
								checkThenAppendOrRecurse(value, method, builder, references);
							}
						}else{
							builder.append("<br>"+method.getName()+" = "+String.valueOf(result));
							references.put(result, result);
						}
					}
				}
			}
		}catch(Throwable t){
			//ignored
		}
	}
	
	private void checkThenAppendOrRecurse(Object bean, Method method, StringBuilder builder, final Map<Object, Object> references){
		if(!references.containsKey(bean)){
			if(bean.getClass().getPackage().getName().startsWith("java")){
				builder.append("<br>"+method.getName()+" = "+String.valueOf(bean));
				references.put(bean, bean);
			}else{
				build(bean, builder, references);
			}
		}
	}
	
}

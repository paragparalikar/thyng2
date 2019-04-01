package com.thyng.gateway.server.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpChannelInitailizer extends ChannelInitializer<SocketChannel>{

	private final HttpStaticFileHandler httpStaticFileHandler = new HttpStaticFileHandler();
	private final SystemMXBeanInfoRequestHandler systemMXBeanInfoRequestHandler = new SystemMXBeanInfoRequestHandler("/system/mx");
	private final SystemPropertiesRequestHandler systemPropertiesRequestHandler = new SystemPropertiesRequestHandler("/system/properties");
	private final SystemEnvironmentVariablesRequestHandler systemEnvironmentVariablesRequestHandler = new SystemEnvironmentVariablesRequestHandler("/system/environment-variables");
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		final ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast(new HttpServerCodec(), 
				new HttpObjectAggregator(512*1024), 
				httpStaticFileHandler, 
				systemMXBeanInfoRequestHandler,
				systemPropertiesRequestHandler, 
				systemEnvironmentVariablesRequestHandler,
				new HttpContentCompressor());
	}

}

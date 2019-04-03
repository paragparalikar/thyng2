package com.thyng.gateway.server.http;

import com.thyng.gateway.server.http.handler.HttpStaticFileHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpChannelInitailizer extends ChannelInitializer<SocketChannel>{

	private final HttpStaticFileHandler httpStaticFileHandler = new HttpStaticFileHandler();
	private final HttpFrontConrollerChannelHandler frontController = new HttpFrontConrollerChannelHandler();
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		final ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast(new HttpServerCodec(), 
				new HttpObjectAggregator(512*1024), 
				httpStaticFileHandler, 
				frontController,
				new HttpContentCompressor());
	}

}

package com.thyng.gateway.server;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thyng.gateway.server.http.HttpChannelInitailizer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

public class ThyngGatewayServer {

	static{
		InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.INSTANCE);
	}
	private static final Logger log = LoggerFactory.getLogger(ThyngGatewayServer.class);
	
	public static void main(String[] args) throws InterruptedException {
		log.info("Attempting to start Thyng Gateway server");
		final int httpPort = Integer.getInteger("thyng.gateway.server.http.port", 9090);
		final int mqttPort = Integer.getInteger("thyng.gateway.server.mqtt.port", 9091);
		final ThyngGatewayServer server = new ThyngGatewayServer();
		server.start(httpPort, mqttPort);
	}

	private ChannelFuture httpChannelFuture;
	private ChannelFuture mqttChannelFuture;
	private final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	public void start(final int httpPort, final int mqttPort) throws InterruptedException {
		try {
			validate();
			prepareShutdownHook();
			httpChannelFuture = prepareHttpChannel(httpPort);
			mqttChannelFuture = prepareMqttChannel(mqttPort);
			log.info("Thyng Gateway server started successfully");
			log.info("Listening for HTTP requests on port : "+httpPort);
			log.info("Listening for MQTT requests on port : "+mqttPort);
			sync(httpChannelFuture, mqttChannelFuture);
		} finally {
			shutdown();
		}
	}
	
	private void validate() {
		if (Objects.nonNull(httpChannelFuture) || Objects.nonNull(mqttChannelFuture)) {
			throw new IllegalStateException("Server is already running");
		}
	}
	
	private ChannelFuture prepareHttpChannel(int httpPort) throws InterruptedException{
		return new ServerBootstrap()
				.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new HttpChannelInitailizer())
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.bind(httpPort).sync();
	}
	
	private ChannelFuture prepareMqttChannel(int mqttPort) throws InterruptedException{
		return new ServerBootstrap()
				.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new HttpChannelInitailizer())
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.bind(mqttPort).sync();
	}
	
	private void sync(ChannelFuture...channelFutures) throws InterruptedException{
		for(ChannelFuture channelFuture : channelFutures){
			channelFuture.channel().closeFuture().sync();
		}
	}
	
	private void prepareShutdownHook(){
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				try {
					shutdown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void shutdown() throws InterruptedException {
		log.info("Thyng Gateway server is shutting down");
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
		httpChannelFuture.channel().close();
		mqttChannelFuture.channel().close();
		log.info("Goodbye");
	}

}

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
		final ThyngGatewayServer server = new ThyngGatewayServer();
		server.start(httpPort);
	}

	private ChannelFuture httpChannelFuture;
	private final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	public void start(final int httpPort) throws InterruptedException {
		try {
			validate();
			prepareShutdownHook();
			httpChannelFuture = prepareHttpChannel(httpPort);
			log.info("Thyng Gateway server started successfully");
			log.info("Listening for HTTP requests on port : "+httpPort);
			httpChannelFuture.channel().closeFuture().sync();
		} finally {
			shutdown();
		}
	}
	
	private void validate() {
		if (Objects.nonNull(httpChannelFuture)) {
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
		log.info("Goodbye");
	}

}
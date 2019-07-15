package com.thyng.netty;  

import java.util.function.Function;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class NettyServer {

	private final int port;
	private ChannelFuture channelFuture;
	private final NioEventLoopGroup parentNioEventLoopGroup = new NioEventLoopGroup();
	private final NioEventLoopGroup childNioEventLoopGroup = new NioEventLoopGroup();
	private final ServerBootstrap serverBootstrap = new ServerBootstrap()
			.group(parentNioEventLoopGroup, childNioEventLoopGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childOption(ChannelOption.SO_KEEPALIVE, true)
			.childHandler(new NettyServerInitializer(this::serve));
	
	public abstract Object serve(final Object request);
	
	@SneakyThrows
	public void start() {
		if(isDone(parentNioEventLoopGroup) || isDone(childNioEventLoopGroup)) {
			throw new IllegalStateException("Server can not be restarted once shutdown");
		}
		log.info("Starting Netty server at port " + port);
		channelFuture = serverBootstrap.bind(port).sync();
		log.info("Successfully started Netty server at port " + port);
	}
	
	private boolean isDone(final NioEventLoopGroup nioEventLoopGroup) {
		return nioEventLoopGroup.isShutdown() || nioEventLoopGroup.isShuttingDown() || nioEventLoopGroup.isTerminated();
	}
	
	public ChannelFuture channelFuture() {
		return channelFuture;
	}
	
	@SneakyThrows
	public void stop() {
		if(null != channelFuture && !channelFuture.isDone()) {
			log.info("Stopping Netty server");
			channelFuture.channel().close();
			channelFuture.channel().parent().close();
			parentNioEventLoopGroup.shutdownGracefully();
			childNioEventLoopGroup.shutdownGracefully();
			channelFuture.channel().closeFuture().sync();
		}
	}	
}

@RequiredArgsConstructor
class NettyServerInitializer extends ChannelInitializer<SocketChannel>{
	
	private final NettyRequestHandler nettyRequestHandler;
	
	public NettyServerInitializer(final Function<Object, Object> function) {
		this.nettyRequestHandler = new NettyRequestHandler(function);
	}
	
	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		socketChannel.pipeline().addLast(
				new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, Integer.BYTES, 0, Integer.BYTES),
				new KryoCodec(),
				nettyRequestHandler);
	}
	
}

@Slf4j
@Sharable
@RequiredArgsConstructor
class NettyRequestHandler extends ChannelInboundHandlerAdapter {
	
	private final Function<Object,Object> function;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		final Object result = function.apply(msg);
		if(null != result) {
			ctx.write(result);
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("Exception caught", cause);
		ctx.close();
	}
}

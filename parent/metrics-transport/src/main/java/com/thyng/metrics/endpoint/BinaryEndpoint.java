package com.thyng.metrics.endpoint;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import com.thyng.model.Metrics;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BinaryEndpoint extends ChannelInitializer<SocketChannel> {

	private final int port;
	private final Executor executor;
	private ChannelFuture channelFuture;
	private ServerBootstrap serverBootstrap;
	private final Consumer<Metrics> metricsConsumer;	

	@Builder
	public BinaryEndpoint(int port, Executor executor, Consumer<Metrics> metricsConsumer) {
		super();
		this.port = port;
		this.executor = executor;
		this.metricsConsumer = metricsConsumer;
	}

	public void start() throws Exception {
		stop();
		log.info("Starting Netty server at port " + port);
		serverBootstrap = new ServerBootstrap()
				.group(new NioEventLoopGroup(), new NioEventLoopGroup())
				.channel(NioServerSocketChannel.class)
				.childHandler(this);
		channelFuture = serverBootstrap.bind(port).sync();
		log.info("Successfully started Netty server at port " + port);
	}

	public void stop() throws Exception {
		if (null != channelFuture && !channelFuture.isDone()) {
			log.info("Stopping Netty server");
			channelFuture.channel().close();
			channelFuture.channel().parent().close();
			serverBootstrap.config().group().shutdownGracefully();
			serverBootstrap.config().childGroup().shutdownGracefully();
			channelFuture.channel().closeFuture().sync();
			log.info("Netty server stopped successfully");
		}
	}

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		socketChannel.pipeline().addLast(
				new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, Integer.BYTES), 
				MetricsResolutionHandler.builder()
				.executor(executor)
				.metricsConsumer(metricsConsumer)
				.build());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("Failed to initialize channel", cause);
		super.exceptionCaught(ctx, cause);
	}

}


@Slf4j
@Builder
@RequiredArgsConstructor
class MetricsResolutionHandler extends ByteToMessageDecoder{
	
	private final Executor executor;
	private final Consumer<Metrics> metricsConsumer;	

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(40 > in.readableBytes()) {
			log.error("Discarding invalid message, message size too small. Min 40 bytes, got " + in.readableBytes());
			in.clear();
			ctx.close();
		}
		
		final Long metricsSchemaId = in.getUnsignedInt(Integer.BYTES);
		final byte[] data = new byte[in.readableBytes()];
		in.getBytes(0, data);
		in.clear();
		ctx.close();
		executor.execute(() -> metricsConsumer.accept(new Metrics(data, metricsSchemaId)));
	}

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		log.error("Failed to resolve metrics", cause);
	}
}

package com.thyng.netty;

import java.util.List;

import com.thyng.KryoSerializer;
import com.thyng.Serializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

public class KryoCodec extends ByteToMessageCodec<Object> {

	private static final Serializer SERIALIZER = KryoSerializer.INSTANCE;

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		out.writeBytes(SERIALIZER.serialize(msg));
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		out.add(SERIALIZER.deserialize(new ByteBufInputStream(in, true)));
	}

}

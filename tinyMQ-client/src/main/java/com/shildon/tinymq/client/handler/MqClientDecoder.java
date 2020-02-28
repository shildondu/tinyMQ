package com.shildon.tinymq.client.handler;

import java.util.List;

import com.shildon.tinymq.util.SerializeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端解码器。
 *
 * @author shildon<shildondu @ gmail.com>
 * @date May 6, 2016
 */
public class MqClientDecoder extends ByteToMessageDecoder {

	private final Class<?> clazz;

	private static final Logger LOGGER = LoggerFactory.getLogger(MqClientDecoder.class);

	public MqClientDecoder(final Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
		LOGGER.debug("enter mq client decode ...");
		final int size = in.readableBytes();
		final byte[] data = new byte[size];
		in.readBytes(data);
		Object object = null;
		
		try {
			object = SerializeUtils.deserialize(data, this.clazz);
		} catch(final IllegalStateException e) {
			LOGGER.error("the request is not mqrequest!", e);
		}
		out.add(object);
	}

}

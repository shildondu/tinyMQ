package com.shildon.tinymq.client.handler;

import com.shildon.tinymq.core.MqRequest;
import com.shildon.tinymq.util.SerializeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端编码器。
 *
 * @author shildon<shildondu @ gmail.com>
 * @date May 6, 2016
 */
public class MqClientEncoder extends MessageToByteEncoder<MqRequest> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MqClientEncoder.class);

	@Override
	protected void encode(final ChannelHandlerContext ctx, final MqRequest msg, final ByteBuf out) throws Exception {
		LOGGER.debug("enter mq client encode ...");
		final byte[] data = SerializeUtils.serialize(msg);
		out.writeBytes(data);
	}

}

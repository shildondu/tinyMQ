package com.shildon.tinymq.server.handler;

import com.shildon.tinymq.core.MqResponse;
import com.shildon.tinymq.util.SerializeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器端编码器。
 *
 * @author shildon<shildondu @ gmail.com>
 * @date May 3, 2016
 */
public class MqServerEncoder extends MessageToByteEncoder<MqResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MqServerEncoder.class);

	@Override
	protected void encode(final ChannelHandlerContext ctx, final MqResponse msg, final ByteBuf out) {
		LOGGER.debug("enter mq server encode ...");
		final byte[] data = SerializeUtils.serialize(msg);
		out.writeBytes(data);
	}

}

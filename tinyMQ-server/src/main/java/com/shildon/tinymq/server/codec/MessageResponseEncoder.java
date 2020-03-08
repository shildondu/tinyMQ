package com.shildon.tinymq.server.codec;

import com.shildon.tinymq.core.model.MessageResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器端编码器。
 *
 * @author shildon
 */
public class MessageResponseEncoder extends MessageToByteEncoder<MessageResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageResponseEncoder.class);

	@Override
	protected void encode(final ChannelHandlerContext ctx, final MessageResponse msg, final ByteBuf out) {
		LOGGER.info("start encode response...");
		final ByteBuf byteBuf = ctx.alloc().buffer();
		msg.encode(byteBuf);
		out.writeBytes(byteBuf);
	}

}

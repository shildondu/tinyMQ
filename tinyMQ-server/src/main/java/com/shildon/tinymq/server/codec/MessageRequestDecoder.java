package com.shildon.tinymq.server.codec;

import java.util.List;

import com.shildon.tinymq.core.model.MessageRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器端解码器。
 *
 * @author shildon
 */
public class MessageRequestDecoder extends ByteToMessageDecoder {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageRequestDecoder.class);

	@Override
	protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) {
		LOGGER.info("start decode request...");
		final MessageRequest messageRequest = new MessageRequest(in);
		out.add(messageRequest);
	}

}

package com.shildon.tinymq.client.codec;

import com.shildon.tinymq.core.model.MessageRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shildon
 */
public class MessageRequestEncoder extends MessageToByteEncoder<MessageRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRequestEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageRequest msg, ByteBuf out) {
        LOGGER.info("start encode request...");
        final ByteBuf byteBuf = ctx.alloc().buffer();
        msg.encode(byteBuf);
        out.writeBytes(byteBuf);
    }

}

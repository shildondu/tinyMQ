package com.shildon.tinymq.core.codec;

import com.shildon.tinymq.core.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息协议编码器。
 *
 * @author shildon
 */
public class MessageProtocolEncoder extends MessageToByteEncoder<MessageProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProtocolEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) {
        LOGGER.info("start encode response...");
        LOGGER.info("the response is [{}]", msg);
        final ByteBuf byteBuf = ctx.alloc().buffer();
        msg.encode(byteBuf);
        out.writeBytes(byteBuf);
    }

}

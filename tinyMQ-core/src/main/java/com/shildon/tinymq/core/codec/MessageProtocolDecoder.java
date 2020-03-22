package com.shildon.tinymq.core.codec;

import com.shildon.tinymq.core.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 消息协议解码器。
 *
 * @author shildon
 */
public class MessageProtocolDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProtocolDecoder.class);

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) {
        LOGGER.info("start decode request...");
        final MessageProtocol messageProtocol = new MessageProtocol(in);
        LOGGER.info("the request is [{}]", messageProtocol);
        out.add(messageProtocol);
    }

}

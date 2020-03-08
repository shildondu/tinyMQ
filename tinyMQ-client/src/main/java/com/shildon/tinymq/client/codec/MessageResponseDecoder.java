package com.shildon.tinymq.client.codec;

import com.shildon.tinymq.core.model.MessageResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author shildon
 */
public class MessageResponseDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageResponseDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        LOGGER.info("start decode response....");
        final MessageResponse messageResponse = new MessageResponse(in);
        out.add(messageResponse);
    }

}

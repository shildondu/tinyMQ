package com.shildon.tinymq.client.handler;

import com.shildon.tinymq.core.model.MessageResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shildon
 */
public class MessageHandler extends SimpleChannelInboundHandler<MessageResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponse msg) {
        LOGGER.info("handle message: [{}]", msg);
    }

}

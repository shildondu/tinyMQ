package com.shildon.tinymq.server.handler;

import com.shildon.tinymq.core.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器端处理器
 *
 * @author shildon
 */
public class MessageHandler extends SimpleChannelInboundHandler<MessageRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final MessageRequest messageRequest) {
        LOGGER.info("start handle request -> {}", messageRequest);
        int operationCode = messageRequest.getHeader().getOperationCode();
        Operation operation = Operation.find(operationCode);
        byte[] serializedBody = messageRequest.getBody().getSerializedData();
        this.handleOperation(operation, serializedBody);
        // todo supply body
        MessageResponseHeader messageResponseHeader = new MessageResponseHeader(MessageResponseCode.SUCCESS, messageRequest.getHeader());
        MessageResponseBody messageResponseBody = new MessageResponseBody();
        MessageResponse messageResponse = new MessageResponse(messageResponseHeader, messageResponseBody);
        // todo separate write and flush.
        ctx.writeAndFlush(messageResponse);
    }

    private void handleOperation(Operation operation, byte[] serializedBody) {

    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        LOGGER.error("server handler error!", cause);
        ctx.close();
    }

}

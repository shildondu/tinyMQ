package com.shildon.tinymq.server.handler;

import com.shildon.tinymq.core.model.*;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import com.shildon.tinymq.server.RegistryChannelTable;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 服务器端处理器
 *
 * @author shildon
 */
public class MessageHandler extends SimpleChannelInboundHandler<MessageRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private Serializer serializer = new ProtostuffSerializer();
    private RegistryChannelTable registryChannelTable = RegistryChannelTable.getInstance();

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final MessageRequest request) {
        LOGGER.info("start handle request -> {}", request);
        int operationCode = request.getHeader().getOperationCode();
        Operation operation = Operation.find(operationCode);
        byte[] serializedRequestBody = request.getBody().getSerializedData();
        switch (operation) {
            case PUBLISH: {
                PublishMessageRequestBody requestBody = this.serializer.deserialize(serializedRequestBody, PublishMessageRequestBody.class);
                List<Channel> registryChannels = this.registryChannelTable.get(requestBody.getTopic());
                // send message to subscribing channel todo it must be sent to topic
                registryChannels.forEach(registryChannel -> {
                    LOGGER.info("handle each channel: [{}]", registryChannel);
                    MessageResponseHeader responseHeader = new MessageResponseHeader(MessageResponseCode.MESSAGE, request.getHeader());
                    SubscribeMessageResponseBody responseBody = new SubscribeMessageResponseBody(requestBody.getTopic(), requestBody.getSerializedMessage());
                    byte[] serializedResponseBody = this.serializer.serialize(responseBody);
                    MessageResponseBody wrappedResponseBody = new MessageResponseBody(serializedResponseBody);
                    MessageResponse response = new MessageResponse(responseHeader, wrappedResponseBody);
                    registryChannel.writeAndFlush(response);
                });
                // send ack
                MessageResponseHeader responseHeader = new MessageResponseHeader(MessageResponseCode.ACK, request.getHeader());
                MessageResponse messageResponse = new MessageResponse(responseHeader);
                ctx.channel().writeAndFlush(messageResponse);
            }
            case SUBSCRIBE: {
                SubscribeMessageRequestBody subscribeRequestBody = serializer.deserialize(serializedRequestBody, SubscribeMessageRequestBody.class);
                this.registryChannelTable.put(subscribeRequestBody.getTopic(), ctx.channel());
            }
        }
    }

}

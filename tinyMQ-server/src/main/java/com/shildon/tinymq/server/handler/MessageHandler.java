package com.shildon.tinymq.server.handler;

import com.shildon.tinymq.core.RegistryTable;
import com.shildon.tinymq.core.model.*;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
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
    private RegistryTable registryTable = RegistryTable.getInstance();

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final MessageRequest messageRequest) {
        LOGGER.info("start handle request -> {}", messageRequest);
        int operationCode = messageRequest.getHeader().getOperationCode();
        Operation operation = Operation.find(operationCode);
        byte[] serializedBody = messageRequest.getBody().getSerializedData();
        switch (operation) {
            case PUBLISH: {
                PublishMessageRequestBody publishMessageRequestBody = serializer.deserialize(serializedBody, PublishMessageRequestBody.class);
                List<Channel> registryChannels = this.registryTable.get(publishMessageRequestBody.getTopic());
                registryChannels.forEach(registryChannel -> {
                    // send message to subscribing channel
                    LOGGER.info("handle each channel: [{}]", registryChannel);
                    MessageResponseHeader messageResponseHeader = new MessageResponseHeader(MessageResponseCode.MESSAGE, messageRequest.getHeader());
                    MessageResponseBody messageResponseBody = new MessageResponseBody(publishMessageRequestBody.getSerializedMessage());
                    MessageResponse messageResponse = new MessageResponse(messageResponseHeader, messageResponseBody);
                    // todo separate write and flush.
                    registryChannel.writeAndFlush(messageResponse);
                });
            }
            case SUBSCRIBE: {
                SubscribeMessageRequestBody subscribeMessageRequestBody = serializer.deserialize(serializedBody, SubscribeMessageRequestBody.class);
                this.registryTable.put(subscribeMessageRequestBody.getTopic(), ctx.channel());
            }
        }
    }

}

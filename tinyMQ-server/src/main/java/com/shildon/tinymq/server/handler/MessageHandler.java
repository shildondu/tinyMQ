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
    protected void channelRead0(final ChannelHandlerContext ctx, final MessageRequest request) {
        LOGGER.info("start handle request -> {}", request);
        int operationCode = request.getHeader().getOperationCode();
        Operation operation = Operation.find(operationCode);
        byte[] serializedBody = request.getBody().getSerializedData();
        switch (operation) {
            case PUBLISH: {
                PublishMessageRequestBody publishRequestBody = serializer.deserialize(serializedBody, PublishMessageRequestBody.class);
                List<Channel> registryChannels = this.registryTable.get(publishRequestBody.getTopic());
                registryChannels.forEach(registryChannel -> {
                    // send message to subscribing channel
                    LOGGER.info("handle each channel: [{}]", registryChannel);
                    MessageResponseHeader responseHeader = new MessageResponseHeader(MessageResponseCode.MESSAGE, request.getHeader());
                    MessageResponseBody responseBody = new MessageResponseBody(publishRequestBody.getSerializedMessage());
                    MessageResponse response = new MessageResponse(responseHeader, responseBody);
                    // todo separate write and flush.
                    registryChannel.writeAndFlush(response);
                });
            }
            case SUBSCRIBE: {
                SubscribeMessageRequestBody subscribeRequestBody = serializer.deserialize(serializedBody, SubscribeMessageRequestBody.class);
                this.registryTable.put(subscribeRequestBody.getTopic(), ctx.channel());
            }
        }
    }

}

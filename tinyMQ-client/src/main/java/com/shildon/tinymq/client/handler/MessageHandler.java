package com.shildon.tinymq.client.handler;

import com.shildon.tinymq.client.RegistryConsumerTable;
import com.shildon.tinymq.core.model.MessageResponse;
import com.shildon.tinymq.core.model.MessageResponseCode;
import com.shildon.tinymq.core.model.SubscribeMessageResponseBody;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author shildon
 */
public class MessageHandler extends SimpleChannelInboundHandler<MessageResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private Serializer defaultSerializer = new ProtostuffSerializer();
    private RegistryConsumerTable registryConsumerTable = RegistryConsumerTable.getInstance();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponse response) {
        LOGGER.info("handle message: [{}]", response);
        MessageResponseCode responseCode = MessageResponseCode.find(response.getHeader().getCode());
        switch (responseCode) {
            case MESSAGE: {
                byte[] wrappedSerializedData = response.getBody().getSerializedData();
                SubscribeMessageResponseBody responseBody = this.defaultSerializer.deserialize(wrappedSerializedData, SubscribeMessageResponseBody.class);
                List<Consumer<SubscribeMessageResponseBody>> consumers = this.registryConsumerTable.get(responseBody.getTopic());
                consumers.forEach(it -> it.accept(responseBody));
            }
            case SUCCESS: {

            }
            case SYSTEM_ERROR: {

            }
        }
    }

}

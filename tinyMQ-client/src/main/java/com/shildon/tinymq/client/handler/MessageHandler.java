package com.shildon.tinymq.client.handler;

import com.shildon.tinymq.client.MessageCache;
import com.shildon.tinymq.client.RegistryConsumerTable;
import com.shildon.tinymq.core.protocol.*;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import com.shildon.tinymq.core.util.MessageIdUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author shildon
 */
public class MessageHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private Serializer defaultSerializer = new ProtostuffSerializer();
    private RegistryConsumerTable registryConsumerTable = RegistryConsumerTable.getInstance();
    private MessageCache messageCache = MessageCache.getInstance();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol response) {
        LOGGER.info("handle message: [{}]", response);
        MessageType messageType = MessageType.find(response.getHeader().getMessageType());
        switch (messageType) {
            case PUBLISH: {
                byte[] wrappedSerializedData = response.getBody().getSerializedData();
                PublishMessageBody publishMessageBody = this.defaultSerializer.deserialize(wrappedSerializedData, PublishMessageBody.class);
                List<Consumer<PublishMessageBody>> consumers = this.registryConsumerTable.get(publishMessageBody.getTopic());
                consumers.forEach(it -> it.accept(publishMessageBody));

                // send ack
                MessageHeader header = new MessageHeader();
                header.setMessageType(MessageType.ACK.getValue());
                header.setMessageId(MessageIdUtils.generate());

                MessageBody body = new MessageBody();
                MessageProtocol ack = new MessageProtocol(header, body);
                ctx.channel().writeAndFlush(ack);
            }
            case ACK: {
                String messageId = String.valueOf(response.getHeader().getMessageId());
                this.messageCache.remove(messageId);
            }
        }
    }

}

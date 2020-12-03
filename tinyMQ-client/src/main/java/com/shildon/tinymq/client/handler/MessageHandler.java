package com.shildon.tinymq.client.handler;

import java.util.List;
import java.util.function.Consumer;

import com.shildon.tinymq.client.MessageCache;
import com.shildon.tinymq.client.RegistryConsumerTable;
import com.shildon.tinymq.core.protocol.MessageProtocol;
import com.shildon.tinymq.core.protocol.MessageType;
import com.shildon.tinymq.core.protocol.PublishMessageBody;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import com.shildon.tinymq.core.util.MessageProtocolUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shildon
 */
public class MessageHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private final Serializer defaultSerializer = new ProtostuffSerializer();
    private final RegistryConsumerTable registryConsumerTable = RegistryConsumerTable.getInstance();
    private final MessageCache messageCache = MessageCache.getInstance();

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final MessageProtocol response) {
        LOGGER.info("handle message: [{}]", response);
        final MessageType messageType = MessageType.find(response.getHeader().getMessageType());
        switch (messageType) {
            case PUBLISH: {
                final byte[] wrappedSerializedData = response.getBody().getSerializedData();
                final PublishMessageBody publishMessageBody = this.defaultSerializer.deserialize(wrappedSerializedData, PublishMessageBody.class);
                final List<Consumer<PublishMessageBody>> consumers = this.registryConsumerTable.get(publishMessageBody.getTopic());
                consumers.forEach(it -> it.accept(publishMessageBody));
                // send ack
                ctx.channel().writeAndFlush(MessageProtocolUtils.ack(response.getHeader().getMessageId()));
                break;
            }
            case ACK: {
                final String messageId = String.valueOf(response.getHeader().getMessageId());
                this.messageCache.remove(messageId);
                break;
            }
            default:
                LOGGER.error("unsupported message type: {}", messageType);
                break;
        }
    }

}

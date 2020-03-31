package com.shildon.tinymq.server.handler;

import com.shildon.tinymq.core.protocol.*;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import com.shildon.tinymq.server.MessageRetryer;
import com.shildon.tinymq.server.cache.MessageCache;
import com.shildon.tinymq.server.cache.RegistryChannelTable;
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
public class MessageHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private Serializer serializer = new ProtostuffSerializer();
    private RegistryChannelTable registryChannelTable = RegistryChannelTable.getInstance();
    private MessageCache messageCache = MessageCache.getInstance();
    private MessageRetryer messageRetryer = MessageRetryer.getInstance();

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final MessageProtocol request) {
        LOGGER.info("start handle request -> {}", request);
        int type = request.getHeader().getMessageType();
        MessageType messageType = MessageType.find(type);
        byte[] serializedRequestBody = request.getBody().getSerializedData();
        switch (messageType) {
            case PUBLISH: {
                PublishMessageBody publishMessageBody = this.serializer.deserialize(serializedRequestBody, PublishMessageBody.class);
                List<Channel> registryChannels = this.registryChannelTable.get(publishMessageBody.getTopic());
                // send message to subscribing channel todo it must be sent to topic and send message to subscriber in other way.
                registryChannels.forEach(registryChannel -> {
                    LOGGER.info("handle each channel: [{}]", registryChannel);

                    MessageProtocol messageProtocol = new MessageProtocol.Builder()
                            .header(
                                    new MessageHeader.Builder()
                                            .messageType(MessageType.PUBLISH)
                                            .build()
                            )
                            .body(
                                    new MessageBody.Builder()
                                            .serializedData(serializedRequestBody)
                                            .build()
                            )
                            .build();
                    registryChannel.writeAndFlush(messageProtocol);

                    messageCache.put(messageProtocol, registryChannel);
                    messageRetryer.retry();
                });
                // send ack
                MessageProtocol ack = new MessageProtocol.Builder()
                        .header(
                                new MessageHeader.Builder()
                                        .messageType(MessageType.ACK)
                                        .messageId(request.getHeader().getMessageId())
                                        .build()
                        )
                        .body(
                                new MessageBody.Builder()
                                        .build()
                        )
                        .build();
                ctx.channel().writeAndFlush(ack);
            }
            case SUBSCRIBE: {
                SubscribeMessageBody subscribeMessageBody = serializer.deserialize(serializedRequestBody, SubscribeMessageBody.class);
                this.registryChannelTable.put(subscribeMessageBody.getTopic(), ctx.channel());
            }
        }
    }

}

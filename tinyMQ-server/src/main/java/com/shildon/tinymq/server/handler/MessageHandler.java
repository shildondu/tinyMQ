package com.shildon.tinymq.server.handler;

import com.shildon.tinymq.core.protocol.*;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import com.shildon.tinymq.core.util.MessageIdUtils;
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
public class MessageHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private Serializer serializer = new ProtostuffSerializer();
    private RegistryChannelTable registryChannelTable = RegistryChannelTable.getInstance();

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
                // send message to subscribing channel todo it must be sent to topic
                registryChannels.forEach(registryChannel -> {
                    LOGGER.info("handle each channel: [{}]", registryChannel);

                    MessageHeader header = new MessageHeader();
                    header.setMessageId(request.getHeader().getMessageId());
                    header.setMessageType(MessageType.SUBSCRIBE.getValue());

                    MessageBody body = new MessageBody(serializedRequestBody);
                    MessageProtocol messageProtocol = new MessageProtocol(header, body);
                    registryChannel.writeAndFlush(messageProtocol);
                });
                // send ack
                MessageHeader header = new MessageHeader();
                header.setMessageType(MessageType.ACK.getValue());
                header.setMessageId(MessageIdUtils.generate());

                MessageBody body = new MessageBody();
                MessageProtocol messageProtocol = new MessageProtocol(header, body);
                ctx.channel().writeAndFlush(messageProtocol);
            }
            case SUBSCRIBE: {
                SubscribeMessageBody subscribeMessageBody = serializer.deserialize(serializedRequestBody, SubscribeMessageBody.class);
                this.registryChannelTable.put(subscribeMessageBody.getTopic(), ctx.channel());
            }
        }
    }

}

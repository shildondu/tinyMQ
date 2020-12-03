package com.shildon.tinymq.server.handler;

import java.util.Set;

import com.shildon.tinymq.core.protocol.MessageProtocol;
import com.shildon.tinymq.core.protocol.MessageType;
import com.shildon.tinymq.core.protocol.PublishMessageBody;
import com.shildon.tinymq.core.protocol.SubscribeMessageBody;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import com.shildon.tinymq.core.util.MessageProtocolUtils;
import com.shildon.tinymq.server.MessageRetryer;
import com.shildon.tinymq.server.cache.MessageCache;
import com.shildon.tinymq.server.cache.RegistryChannelTable;
import com.shildon.tinymq.server.cache.TopicCache;
import com.shildon.tinymq.server.model.Subscriber;
import com.shildon.tinymq.server.model.Topic;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器端处理器
 *
 * @author shildon
 */
public class MessageHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private final Serializer serializer = new ProtostuffSerializer();
    private final RegistryChannelTable registryChannelTable = RegistryChannelTable.getInstance();
    private final TopicCache topicCache = TopicCache.getInstance();
    private final MessageCache messageCache = MessageCache.getInstance();
    private final MessageRetryer messageRetryer = MessageRetryer.getInstance();

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final MessageProtocol request) {
        LOGGER.info("start handle request -> {}", request);
        final int type = request.getHeader().getMessageType();
        final MessageType messageType = MessageType.find(type);
        final byte[] serializedRequestBody = request.getBody().getSerializedData();
        switch (messageType) {
            case PUBLISH: {
                final PublishMessageBody publishMessageBody = this.serializer.deserialize(serializedRequestBody, PublishMessageBody.class);
                // send message to topic
                final Topic topic = this.topicCache.getTopic(publishMessageBody.getTopic());
                final int queueIndex = topic.offer(publishMessageBody.getSerializedMessage());
                final Set<Subscriber> subscribers = topic.getSubscribers(queueIndex);
                // todo push to subscribers
                // return ack to publisher
                ctx.channel().writeAndFlush(MessageProtocolUtils.ack(request.getHeader().getMessageId()));
                break;
            }
            case SUBSCRIBE: {
                final SubscribeMessageBody subscribeMessageBody = this.serializer.deserialize(serializedRequestBody, SubscribeMessageBody.class);
                Subscriber subscriber = new Subscriber(subscribeMessageBody.getTopic(), subscribeMessageBody.getGroup(), ctx.channel());
                this.registryChannelTable.put(subscribeMessageBody.getTopic(), ctx.channel());
                break;
            }
            default:
                LOGGER.error("unsupported message type: {}", messageType);
                break;
        }
    }

}

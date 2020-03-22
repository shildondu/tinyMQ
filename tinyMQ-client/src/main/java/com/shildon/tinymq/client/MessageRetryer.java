package com.shildon.tinymq.client;

import com.shildon.tinymq.core.protocol.MessageProtocol;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 未收到ack的消息重试器
 *
 * @author shildon
 */
public class MessageRetryer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRetryer.class);
    private static final MessageRetryer INSTANCE = new MessageRetryer();

    private static final long TIME_TO_WAIT = 60 * 1000;

    private MessageClient messageClient = MessageClient.getInstance();
    private MessageCache messageCache = MessageCache.getInstance();

    private MessageRetryer() {

    }

    public static MessageRetryer getInstance() {
        return INSTANCE;
    }

    public void retry() {
        Map<String, MessageProtocol> allMessageRequest = messageCache.getAll();
        allMessageRequest.values()
                .stream()
                .filter(it -> it.getHeader().getTimestamp() + TIME_TO_WAIT < System.currentTimeMillis())
                .forEach(it -> {
                    Channel channel = null;
                    try {
                        try {
                            channel = messageClient.borrowChannel();
                            channel.writeAndFlush(it);
                        } finally {
                            messageClient.returnChannel(channel);
                        }
                    } catch (Exception e) {
                        LOGGER.warn("retry sending request error, message: {}", it);
                    }
                });
    }

}

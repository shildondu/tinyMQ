package com.shildon.tinymq.client;

import com.shildon.tinymq.core.protocol.MessageProtocol;
import com.shildon.tinymq.core.transport.NettyClient;
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

    private static final long TIME_TO_WAIT = 60 * 1000;

    private NettyClient client;
    private MessageCache messageCache = MessageCache.getInstance();

    public MessageRetryer(NettyClient client) {
        this.client = client;
    }

    public void retry(MessageProtocol request) {
        this.messageCache.put(request);
        Map<String, MessageProtocol> allMessageRequest = messageCache.getAll();
        allMessageRequest.values()
                .stream()
                .filter(it -> it.getHeader().getTimestamp() + TIME_TO_WAIT < System.currentTimeMillis())
                .forEach(it -> {
                    try {
                        this.client.write(it);
                    } catch (Exception e) {
                        LOGGER.warn("retry sending request error, message: {}", it);
                    }
                });
    }

}

package com.shildon.tinymq.core.exchange;

import com.shildon.tinymq.core.protocol.MessageProtocol;
import com.shildon.tinymq.core.transport.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shildon
 */
public class MessageExchange {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageExchange.class);

    private static final Map<String, MessageFuture> MESSAGE_FUTURE_MAP = new ConcurrentHashMap<>();

    private Client client;

    public MessageExchange(Client client) {
        this.client = client;
    }

    public MessageFuture exchange(MessageProtocol request) {
        MessageFuture messageFuture = new MessageFuture();
        try {
            this.client.write(request);
            MESSAGE_FUTURE_MAP.put(request.getHeader().getMessageId(), messageFuture);
        } catch (Exception e) {
            LOGGER.error("write to channel failed, request: {}", request, e);
        }
        return messageFuture;
    }

}

package com.shildon.tinymq.client;

import com.shildon.tinymq.core.protocol.MessageProtocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 发送消息缓存，当收到ack后才删除，确保发送端消息不丢
 *
 * @author shildon
 */
public class MessageCache {

    private static final MessageCache MESSAGE_CACHE = new MessageCache();

    private Map<String, MessageProtocol> messageMap = new ConcurrentHashMap<>();

    public static MessageCache getInstance() {
        return MESSAGE_CACHE;
    }

    private MessageCache() {
    }

    public void put(MessageProtocol request) {
        this.messageMap.put(String.valueOf(request.getHeader().getMessageId()), request);
    }

    public MessageProtocol remove(String messageId) {
        return this.messageMap.remove(messageId);
    }

}

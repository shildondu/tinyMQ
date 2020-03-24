package com.shildon.tinymq.server.cache;

import com.shildon.tinymq.core.model.Pair;
import com.shildon.tinymq.core.protocol.MessageProtocol;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 发送消息缓存，当收到ack后才删除，确保发送端消息不丢
 *
 * @author shildon
 */
public class MessageCache {

    private static final MessageCache MESSAGE_CACHE = new MessageCache();

    private Map<String, Pair<MessageProtocol, Channel>> messageMap = new ConcurrentHashMap<>();

    public static MessageCache getInstance() {
        return MESSAGE_CACHE;
    }

    private MessageCache() {
    }

    public void put(MessageProtocol request, Channel channel) {
        this.messageMap.put(String.valueOf(request.getHeader().getMessageId()), new Pair<>(request, channel));
    }

    public Map<String, Pair<MessageProtocol, Channel>> getAll() {
        return this.messageMap;
    }

    public Pair<MessageProtocol, Channel> remove(String messageId) {
        return this.messageMap.remove(messageId);
    }

}

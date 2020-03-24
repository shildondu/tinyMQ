package com.shildon.tinymq.server.cache;

import com.shildon.tinymq.server.model.Topic;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shildon
 */
public final class TopicCache {

    private static final TopicCache INSTANCE = new TopicCache();

    private Map<String, Topic> topicMap = new ConcurrentHashMap<>();

    private TopicCache() {

    }

    public static TopicCache getInstance() {
        return INSTANCE;
    }

    public void put(String topicName, byte[] message) {
        Topic topic = topicMap.get(topicName);
        if (topic == null) {
            throw new NoSuchElementException();
        }
        topic.offer(message);
    }

    public void create(String topicName) {
        this.topicMap.put(topicName, new Topic(topicName));
    }

}

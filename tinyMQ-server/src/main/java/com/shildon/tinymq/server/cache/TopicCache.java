package com.shildon.tinymq.server.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shildon.tinymq.server.model.Topic;

/**
 * @author shildon
 */
public final class TopicCache {

    private static final TopicCache INSTANCE = new TopicCache();

    private final Map<String, Topic> topicMap = new ConcurrentHashMap<>();

    private TopicCache() {
        // todo just for test
        final Topic topic = new Topic("shildon.test");
        this.topicMap.put(topic.getName(), topic);
    }

    public static TopicCache getInstance() {
        return INSTANCE;
    }

    public Topic getTopic(final String topicName) {
        return this.topicMap.get(topicName);
    }

}

package com.shildon.tinymq.server.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shildon
 */
public class Group {
    private String name;
    private Map<String, Subscriber> subscriberMap = new ConcurrentHashMap<>();

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, Subscriber> getSubscriberMap() {
        return subscriberMap;
    }
}

package com.shildon.tinymq.core.model;

/**
 * @author shildon
 */
public class SubscribeMessageRequestBody {
    private String topic;

    public SubscribeMessageRequestBody() {
    }

    public SubscribeMessageRequestBody(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}

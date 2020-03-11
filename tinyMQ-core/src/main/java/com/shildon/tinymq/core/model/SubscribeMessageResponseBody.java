package com.shildon.tinymq.core.model;

/**
 * @author shildon
 */
public class SubscribeMessageResponseBody {
    private String topic;
    private byte[] serializedMessage;

    public SubscribeMessageResponseBody() {
    }

    public SubscribeMessageResponseBody(String topic, byte[] serializedMessage) {
        this.topic = topic;
        this.serializedMessage = serializedMessage;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getSerializedMessage() {
        return serializedMessage;
    }

    public void setSerializedMessage(byte[] serializedMessage) {
        this.serializedMessage = serializedMessage;
    }
}

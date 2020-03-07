package com.shildon.tinymq.core;

/**
 * @author shildon
 */
public class PublishMessageRequestBody {
    private String topic;
    private byte[] serializedMessage;

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

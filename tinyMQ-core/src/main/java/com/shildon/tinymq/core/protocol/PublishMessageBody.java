package com.shildon.tinymq.core.protocol;

import java.util.Arrays;

/**
 * 发布消息的消息体
 *
 * @author shildon
 */
public class PublishMessageBody {
    private String topic;
    private byte[] serializedMessage;

    public PublishMessageBody() {
    }

    @Override
    public String toString() {
        return "PublishMessageBody{" +
                "topic='" + topic + '\'' +
                ", serializedMessage=" + Arrays.toString(serializedMessage) +
                '}';
    }

    public PublishMessageBody(String topic, byte[] serializedMessage) {
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

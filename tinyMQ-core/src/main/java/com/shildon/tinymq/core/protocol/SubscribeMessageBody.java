package com.shildon.tinymq.core.protocol;

/**
 * 订阅消息的消息体
 *
 * @author shildon
 */
public class SubscribeMessageBody {
    private String topic;
    private String group;

    public SubscribeMessageBody() {
    }

    @Override
    public String toString() {
        return "SubscribeMessageBody{" +
                "topic='" + topic + '\'' +
                ", group='" + group + '\'' +
                '}';
    }

    public SubscribeMessageBody(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}

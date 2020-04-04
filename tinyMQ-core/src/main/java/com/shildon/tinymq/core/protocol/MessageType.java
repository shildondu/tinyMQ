package com.shildon.tinymq.core.protocol;

import java.util.Arrays;

/**
 * 消息类型
 *
 * @author shildon
 */
public enum MessageType {
    /**
     * publish
     */
    PUBLISH(100),

    /**
     * subscribe
     */
    SUBSCRIBE(200),

    /**
     * poll
     */
    POLL(201),

    /**
     * get server list
     */
    GET_SERVER_LIST(202),

    /**
     * register server
     */
    REGISTER_SERVER(300),

    /**
     * create topic
     */
    CREATE_TOPIC(400),

    /**
     * ack
     */
    ACK(900);

    private int value;

    MessageType(int value) {
        this.value = value;
    }

    public static MessageType find(int value) {
        return Arrays.stream(MessageType.values())
                .filter(it -> it.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("value: [%s] is not a message type", value)));
    }

    public int getValue() {
        return value;
    }
}

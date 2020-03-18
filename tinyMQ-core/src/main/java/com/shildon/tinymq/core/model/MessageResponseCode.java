package com.shildon.tinymq.core.model;

import java.util.Arrays;
import java.util.List;

/**
 * @author shildon
 */
public enum MessageResponseCode {
    /**
     * ack
     */
    ACK(200),

    /**
     * system error
     */
    SYSTEM_ERROR(500),

    /**
     * response with message
     */
    MESSAGE(600);

    private int value;

    public static MessageResponseCode find(int code) {
        final List<MessageResponseCode> responseCodes = Arrays.asList(MessageResponseCode.values());
        return responseCodes.stream()
                .filter(it -> it.value == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("code: [%s] has no operation", code)));
    }

    MessageResponseCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

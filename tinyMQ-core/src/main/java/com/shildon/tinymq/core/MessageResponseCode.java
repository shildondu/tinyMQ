package com.shildon.tinymq.core;

/**
 * @author shildon
 */
public enum MessageResponseCode {
    /**
     * success
     */
    SUCCESS(200),

    /**
     * system error
     */
    SYSTEM_ERROR(500);

    private int value;

    MessageResponseCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

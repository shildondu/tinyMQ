package com.shildon.tinymq.core.util;

import java.nio.charset.StandardCharsets;

import com.shildon.tinymq.core.exception.MessageIdGenerationException;

/**
 * 消息id工具类
 *
 * @author shildon
 */
public final class MessageIdUtils {

    private static final int MESSAGE_ID_LENGTH = 16;

    private MessageIdUtils() {}

    public static String generate() {
        final StringBuilder messageIdBuilder = new StringBuilder(String.valueOf(System.currentTimeMillis()));
        while (messageIdBuilder.length() < MESSAGE_ID_LENGTH) {
            messageIdBuilder.append("0");
        }
        final String messageId = messageIdBuilder.toString();
        if (messageId.getBytes(StandardCharsets.UTF_8).length != MESSAGE_ID_LENGTH) {
            throw new MessageIdGenerationException();
        }
        return messageId;
    }

}

package com.shildon.tinymq.core.util;

import com.shildon.tinymq.core.exception.MessageIdGenerationException;

import java.nio.charset.StandardCharsets;

/**
 * 消息id工具类
 *
 * @author shildon
 */
public final class MessageIdUtils {

    public static String generate() {
        // todo refactor message id generation.
        String messageId = "1234567890123456";
        if (messageId.getBytes(StandardCharsets.UTF_8).length != 16) {
            throw new MessageIdGenerationException();
        }
        return messageId;
    }

}

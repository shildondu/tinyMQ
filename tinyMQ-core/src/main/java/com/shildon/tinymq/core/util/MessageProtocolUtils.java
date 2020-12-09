package com.shildon.tinymq.core.util;

import com.shildon.tinymq.core.protocol.MessageBody;
import com.shildon.tinymq.core.protocol.MessageHeader;
import com.shildon.tinymq.core.protocol.MessageProtocol;
import com.shildon.tinymq.core.protocol.MessageType;

/**
 * @author shildon
 */
public final class MessageProtocolUtils {

    private MessageProtocolUtils() {}

    public static MessageProtocol ack(final String messageId) {
        return message(MessageType.ACK, messageId, null);
    }

    public static MessageProtocol publish(final String messageId, final byte[] serializedData) {
        return message(MessageType.PUBLISH, messageId, serializedData);
    }

    public static MessageProtocol message(final MessageType messageType, final String messageId, final byte[] serializedData) {
        final MessageProtocol.Builder messageProtocolBuilder = new MessageProtocol.Builder()
                .header(
                        new MessageHeader.Builder()
                                .messageType(messageType)
                                .messageId(messageId)
                                .build()
                );

        final MessageBody.Builder messageBodyBuilder = new MessageBody.Builder();
        if (serializedData != null && serializedData.length > 0) {
            messageBodyBuilder.serializedData(serializedData);
        }
        return messageProtocolBuilder.body(messageBodyBuilder.build())
                .build();
    }

}

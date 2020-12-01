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
        return new MessageProtocol.Builder()
                .header(
                        new MessageHeader.Builder()
                                .messageType(MessageType.ACK)
                                .messageId(messageId)
                                .build()
                )
                .body(
                        new MessageBody.Builder()
                                .build()
                )
                .build();
    }

}

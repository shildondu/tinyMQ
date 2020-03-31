package com.shildon.tinymq.core.protocol;

import com.shildon.tinymq.core.constant.Constant;
import com.shildon.tinymq.core.util.MessageIdUtils;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * 消息头部
 *
 * @author shildon
 */
public class MessageHeader {
    // 4byte version
    private int version;
    // 16byte messageId
    private String messageId;
    // 4byte message type
    private int messageType;
    // 8byte timestamp
    private long timestamp;

    private MessageHeader(Builder builder) {
        this.version = builder.version;
        this.messageId = builder.messageId;
        this.messageType = builder.messageTyp;
        this.timestamp = builder.timestamp;
    }

    private MessageHeader(ByteBuf byteBuf) {
        this.decode(byteBuf);
    }

    public void decode(ByteBuf byteBuf) {
        this.version = byteBuf.readInt();
        byte[] messageIdBytes = new byte[16];
        byteBuf.readBytes(messageIdBytes);
        this.messageId = new String(messageIdBytes, StandardCharsets.UTF_8);
        this.messageType = byteBuf.readInt();
        this.timestamp = byteBuf.readLong();
    }

    public void encode(ByteBuf byteBuf) {
        byteBuf.writeInt(this.version);
        byte[] messageIdBytes = this.messageId.getBytes(StandardCharsets.UTF_8);
        byteBuf.writeBytes(messageIdBytes);
        byteBuf.writeInt(this.messageType);
        byteBuf.writeLong(this.timestamp);
    }

    public static class Builder {
        private int version = Constant.CURRENT_PROTOCOL_VERSION;
        private String messageId;
        private int messageTyp;
        private long timestamp = System.currentTimeMillis();

        public Builder version(int version) {
            this.version = version;
            return this;
        }

        public Builder messageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder messageType(MessageType messageType) {
            this.messageTyp = messageType.getValue();
            return this;
        }

        public MessageHeader build() {
            if (null == messageId || "".equals(messageId.trim())) {
                this.messageId = MessageIdUtils.generate();
            }
            return new MessageHeader(this);
        }
    }

    public static class ByteBufBuilder {
        public MessageHeader build(ByteBuf byteBuf) {
            return new MessageHeader(byteBuf);
        }
    }

    @Override
    public String toString() {
        return "MessageProtocolHeader{" +
                "version=" + version +
                ", messageId='" + messageId + '\'' +
                ", messageType=" + messageType +
                ", timestamp=" + timestamp +
                '}';
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

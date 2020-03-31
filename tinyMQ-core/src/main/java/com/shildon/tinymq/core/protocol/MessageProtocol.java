package com.shildon.tinymq.core.protocol;

import io.netty.buffer.ByteBuf;

/**
 * 消息传输协议
 *
 * @author shildon
 */
public class MessageProtocol {
    private MessageHeader header;
    private MessageBody body;

    private MessageProtocol(Builder builder) {
        this.header = builder.header;
        this.body = builder.body;
    }

    private MessageProtocol(ByteBuf byteBuf) {
        this.decode(byteBuf);
    }

    public static class Builder {
        private MessageHeader header;
        private MessageBody body;

        public Builder header(MessageHeader header) {
            this.header = header;
            return this;
        }

        public Builder body(MessageBody body) {
            this.body = body;
            return this;
        }

        public MessageProtocol build() {
            return new MessageProtocol(this);
        }
    }

    public static class ByteBufBuilder {
        public MessageProtocol build(ByteBuf byteBuf) {
            return new MessageProtocol(byteBuf);
        }
    }

    public void decode(ByteBuf byteBuf) {
        this.header = new MessageHeader.ByteBufBuilder()
                .build(byteBuf);
        this.body = new MessageBody.ByteBufBuilder()
                .build(byteBuf);
    }

    public void encode(ByteBuf byteBuf) {
        this.header.encode(byteBuf);
        this.body.encode(byteBuf);
    }

    @Override
    public String toString() {
        return "MessageProtocol{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }

    public MessageHeader getHeader() {
        return header;
    }

    public MessageBody getBody() {
        return body;
    }
}

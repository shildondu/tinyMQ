package com.shildon.tinymq.core;

import io.netty.buffer.ByteBuf;

/**
 * @author shildon
 */
public class Message<T> {
    private MessageHeader header;
    private MessageBody<T> body;

    public Message(final MessageHeader header, final MessageBody<T> body) {
        this.header = header;
        this.body = body;
    }

    public Message(final ByteBuf byteBuf) {
        this.decode(byteBuf);
    }

    public void decode(final ByteBuf byteBuf) {
        this.header = new MessageHeader(byteBuf);
        this.body = new MessageBody<>(byteBuf, Operation.find(this.header.getOperationCode()).getBodyType());
    }

    public void encode(final ByteBuf byteBuf) {
        this.header.encode(byteBuf);
        this.body.encode(byteBuf);
    }

    @Override
    public String toString() {
        return "Message{" +
                "header=" + this.header +
                ", body=" + this.body +
                '}';
    }

    public MessageHeader getHeader() {
        return this.header;
    }

    public void setHeader(final MessageHeader header) {
        this.header = header;
    }

    public MessageBody<T> getBody() {
        return this.body;
    }

    public void setBody(final MessageBody<T> body) {
        this.body = body;
    }
}

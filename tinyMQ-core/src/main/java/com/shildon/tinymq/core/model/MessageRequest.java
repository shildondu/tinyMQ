package com.shildon.tinymq.core.model;

import io.netty.buffer.ByteBuf;

/**
 * @author shildon
 */
public class MessageRequest {
    private MessageRequestHeader header;
    private MessageRequestBody body;

    public MessageRequest(final MessageRequestHeader header, final MessageRequestBody body) {
        this.header = header;
        this.body = body;
    }

    public MessageRequest(final ByteBuf byteBuf) {
        this.decode(byteBuf);
    }

    public void decode(final ByteBuf byteBuf) {
        this.header = new MessageRequestHeader(byteBuf);
        this.body = new MessageRequestBody(byteBuf);
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

    public MessageRequestHeader getHeader() {
        return this.header;
    }

    public void setHeader(final MessageRequestHeader header) {
        this.header = header;
    }

    public MessageRequestBody getBody() {
        return this.body;
    }

    public void setBody(final MessageRequestBody body) {
        this.body = body;
    }
}

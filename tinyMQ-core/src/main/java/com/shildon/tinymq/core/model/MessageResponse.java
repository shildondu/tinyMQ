package com.shildon.tinymq.core.model;

import io.netty.buffer.ByteBuf;

/**
 * @author shildon
 */
public class MessageResponse {
    private MessageResponseHeader header;
    private MessageResponseBody body;

    public MessageResponse(final MessageResponseHeader header, final MessageResponseBody body) {
        this.header = header;
        this.body = body;
    }

    public MessageResponse(final ByteBuf byteBuf) {
        this.decode(byteBuf);
    }

    public void decode(final ByteBuf byteBuf) {
        this.header = new MessageResponseHeader(byteBuf);
        this.body = new MessageResponseBody(byteBuf);
    }

    public void encode(final ByteBuf byteBuf) {
        this.header.encode(byteBuf);
        this.body.encode(byteBuf);
    }

    public MessageResponseHeader getHeader() {
        return this.header;
    }

    public void setHeader(final MessageResponseHeader header) {
        this.header = header;
    }

    public MessageResponseBody getBody() {
        return this.body;
    }

    public void setBody(final MessageResponseBody body) {
        this.body = body;
    }
}

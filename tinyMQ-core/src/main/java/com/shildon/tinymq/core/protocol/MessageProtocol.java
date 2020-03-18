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

    public MessageProtocol() {

    }

    public MessageProtocol(MessageHeader header, MessageBody body) {
        this.header = header;
        this.body = body;
    }

    public MessageProtocol(ByteBuf byteBuf) {
        this.decode(byteBuf);
    }

    public void decode(ByteBuf byteBuf) {
        this.header = new MessageHeader(byteBuf);
        this.body = new MessageBody(byteBuf);
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

    public void setHeader(MessageHeader header) {
        this.header = header;
    }

    public MessageBody getBody() {
        return body;
    }

    public void setBody(MessageBody body) {
        this.body = body;
    }
}

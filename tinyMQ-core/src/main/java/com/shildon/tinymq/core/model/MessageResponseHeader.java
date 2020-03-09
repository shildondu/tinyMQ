package com.shildon.tinymq.core.model;

import io.netty.buffer.ByteBuf;

/**
 * @author shildon
 */
public class MessageResponseHeader {
    private long id;
    private int code;
    private int version;

    public MessageResponseHeader(final long id, final int code, final int version) {
        this.id = id;
        this.code = code;
        this.version = version;
    }

    public MessageResponseHeader(final ByteBuf byteBuf) {
        this.decode(byteBuf);
    }

    public MessageResponseHeader(final MessageResponseCode responseCode, final MessageRequestHeader requestHeader) {
        this.id = requestHeader.getId();
        this.code = responseCode.getValue();
        this.version = requestHeader.getVersion();
    }

    public void decode(final ByteBuf byteBuf) {
        this.id = byteBuf.readLong();
        this.code = byteBuf.readInt();
        this.version = byteBuf.readInt();
    }

    public void encode(final ByteBuf byteBuf) {
        byteBuf.writeLong(this.id);
        byteBuf.writeInt(this.code);
        byteBuf.writeInt(this.version);
    }

    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }
}

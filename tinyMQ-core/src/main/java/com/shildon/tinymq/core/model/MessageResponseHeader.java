package com.shildon.tinymq.core.model;

import io.netty.buffer.ByteBuf;

/**
 * @author shildon
 */
public class MessageResponseHeader {
    private long id;
    private int code;
    private int version;
    private int operationCode;

    public MessageResponseHeader(final long id, final int code, final int version, final int operationCode) {
        this.id = id;
        this.code = code;
        this.version = version;
        this.operationCode = operationCode;
    }

    public MessageResponseHeader(final ByteBuf byteBuf) {
        this.decode(byteBuf);
    }

    public MessageResponseHeader(final MessageResponseCode responseCode, final MessageRequestHeader requestHeader) {
        this.id = requestHeader.getId();
        this.code = responseCode.getValue();
        this.version = requestHeader.getVersion();
        this.operationCode = requestHeader.getOperationCode();
    }

    public void decode(final ByteBuf byteBuf) {
        this.id = byteBuf.readLong();
        this.code = byteBuf.readInt();
        this.version = byteBuf.readInt();
        this.operationCode = byteBuf.readInt();
    }

    public void encode(final ByteBuf byteBuf) {
        byteBuf.writeLong(this.id);
        byteBuf.writeInt(this.code);
        byteBuf.writeInt(this.version);
        byteBuf.writeInt(this.operationCode);
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

    public int getOperationCode() {
        return this.operationCode;
    }

    public void setOperationCode(final int operationCode) {
        this.operationCode = operationCode;
    }
}

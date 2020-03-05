package com.shildon.tinymq.core;

import io.netty.buffer.ByteBuf;

/**
 * @author shildon
 */
public class MessageHeader {
    private long id;
    private int version;
    private int operationCode;

    public MessageHeader(final long id, final int version, final int operationCode) {
        this.id = id;
        this.version = version;
        this.operationCode = operationCode;
    }

    public MessageHeader(final ByteBuf byteBuf) {
        this.decode(byteBuf);
    }

    public void decode(final ByteBuf byteBuf) {
        this.id = byteBuf.readLong();
        this.version = byteBuf.readInt();
        this.operationCode = byteBuf.readInt();
    }

    public void encode(final ByteBuf byteBuf) {
        byteBuf.writeLong(this.id);
        byteBuf.writeInt(this.version);
        byteBuf.writeInt(this.operationCode);
    }

    @Override
    public String toString() {
        return "MessageHeader{" +
                "id=" + this.id +
                ", version=" + this.version +
                ", operationCode=" + this.operationCode +
                '}';
    }

    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
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

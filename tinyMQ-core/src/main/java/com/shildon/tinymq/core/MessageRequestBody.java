package com.shildon.tinymq.core;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;

/**
 * @author shildon
 */
public class MessageRequestBody {
    private byte[] serializedData;

    public MessageRequestBody(final byte[] data) {
        this.serializedData = data;
    }

    public MessageRequestBody(final ByteBuf byteBuf) {
        this.decode(byteBuf);
    }

    public void decode(final ByteBuf byteBuf) {
        final int size = byteBuf.writerIndex() - byteBuf.readerIndex();
        final byte[] data = new byte[size];
        byteBuf.readBytes(data);
        this.serializedData = data;
    }

    public void encode(final ByteBuf byteBuf) {
        byteBuf.writeBytes(this.serializedData);
    }

    @Override
    public String toString() {
        return "MessageBody{" +
                "data=" + Arrays.toString(this.serializedData) +
                '}';
    }

    public byte[] getSerializedData() {
        return this.serializedData;
    }

    public void setSerializedData(final byte[] serializedData) {
        this.serializedData = serializedData;
    }
}

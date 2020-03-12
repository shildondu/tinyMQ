package com.shildon.tinymq.core.model;

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
        if (size <= 0) {
            return;
        }
        final byte[] data = new byte[size];
        byteBuf.readBytes(data);
        this.serializedData = data;
    }

    public void encode(final ByteBuf byteBuf) {
        if (serializedData == null || serializedData.length == 0) {
            return;
        }
        byteBuf.writeBytes(this.serializedData);
    }

    @Override
    public String toString() {
        return "MessageRequestBody{" +
                "serializedData=" + Arrays.toString(this.serializedData) +
                '}';
    }

    public byte[] getSerializedData() {
        return this.serializedData;
    }

    public void setSerializedData(final byte[] serializedData) {
        this.serializedData = serializedData;
    }
}
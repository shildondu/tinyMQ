package com.shildon.tinymq.core.protocol;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * 消息体
 *
 * @author shildon
 */
public class MessageBody {
    private byte[] serializedData;

    public MessageBody() {

    }

    public MessageBody(final byte[] data) {
        this.serializedData = data;
    }

    public MessageBody(final ByteBuf byteBuf) {
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
        return "MessageProtocolBody{" +
                "serializedData=" + Arrays.toString(serializedData) +
                '}';
    }

    public byte[] getSerializedData() {
        return serializedData;
    }

    public void setSerializedData(byte[] serializedData) {
        this.serializedData = serializedData;
    }
}

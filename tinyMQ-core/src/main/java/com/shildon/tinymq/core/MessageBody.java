package com.shildon.tinymq.core;

import com.shildon.tinymq.util.SerializeUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author shildon
 */
public class MessageBody<T> {
    private T data;

    public MessageBody(final T data) {
        this.data = data;
    }

    public MessageBody(final ByteBuf byteBuf, final Class<?> type) {
        this.decode(byteBuf, type);
    }

    public void decode(final ByteBuf byteBuf, final Class<?> type) {
        final int size = byteBuf.writerIndex() - byteBuf.readerIndex();
        final byte[] bytes = new byte[size];
        byteBuf.readBytes(bytes);
        this.data = (T) SerializeUtils.deserialize(bytes, type);
    }

    public void encode(final ByteBuf byteBuf) {
        byteBuf.writeBytes(SerializeUtils.serialize(this.data));
    }

    @Override
    public String toString() {
        return "MessageBody{" +
                "data=" + this.data +
                '}';
    }

    public T getData() {
        return this.data;
    }

    public void setData(final T data) {
        this.data = data;
    }
}

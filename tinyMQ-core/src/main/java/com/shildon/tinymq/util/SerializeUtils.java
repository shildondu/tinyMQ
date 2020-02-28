package com.shildon.tinymq.util;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * 序列化工具。
 *
 * @author shildon<shildondu @ gmail.com>
 * @date May 2, 2016
 */
public final class SerializeUtils {

    private SerializeUtils() {}

    /**
     * 序列化
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(final T obj) {
        final Class<T> cls = (Class<T>) obj.getClass();
        final LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            final Schema<T> schema = RuntimeSchema.getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (final Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化
     */
    public static <T> T deserialize(final byte[] data, final Class<T> cls) {
        try {
            final T message = cls.newInstance();
            final Schema<T> schema = RuntimeSchema.getSchema(cls);
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (final Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}

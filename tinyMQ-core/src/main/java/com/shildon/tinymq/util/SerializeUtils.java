package com.shildon.tinymq.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * 序列化工具。
 *
 * @author shildon<shildondu @ gmail.com>
 * @date May 2, 2016
 */
public final class SerializeUtils {

    private static final Map<Class<?>, Schema<?>> CACHED_SCHEMA = new ConcurrentHashMap<>();

    private SerializeUtils() {}

    /**
     * 获取默认的schema
     */
    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(final Class<T> cls) {
        Schema<T> schema = (Schema<T>) CACHED_SCHEMA.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            CACHED_SCHEMA.put(cls, schema);
        }
        return schema;
    }

    /**
     * 序列化
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(final T obj) {
        final Class<T> cls = (Class<T>) obj.getClass();
        final LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            final Schema<T> schema = getSchema(cls);
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
            final Schema<T> schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (final Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}

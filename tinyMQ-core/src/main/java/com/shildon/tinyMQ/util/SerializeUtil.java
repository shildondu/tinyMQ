package com.shildon.tinyMQ.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * 序列化工具。
 * @author shildon<shildondu@gmail.com>
 * @date May 2, 2016
 */
public class SerializeUtil {
	
	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();
	
	/**
	 * 获取默认的schema
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> Schema<T> getSchema(Class<T> cls) {
		Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null) {
                cachedSchema.put(cls, schema);
            }
        }
        return schema;
	}

	/**
	 * 序列化
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> byte[] serialize(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }
	
	/**
	 * 反序列化
	 * @param data
	 * @param cls
	 * @return
	 */
	public static <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            T message = (T) cls.newInstance();
            Schema<T> schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}

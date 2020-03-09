package com.shildon.tinymq.core.serializer;

/**
 * @author shildon
 */
public interface Serializer {

    <T> byte[] serialize(T object);

    <T> T deserialize(byte[] bytes, Class<T> clazz);

}

package com.shildon.tinymq.client.publisher;

import com.shildon.tinymq.core.serializer.Serializer;

/**
 * @author shildon
 */
public class PublisherFactory {

    public static <T> Publisher<T> create(Serializer serializer) {
        return new Publisher<>(serializer);
    }

}

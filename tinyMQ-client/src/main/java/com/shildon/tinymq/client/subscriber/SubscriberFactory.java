package com.shildon.tinymq.client.subscriber;

import com.shildon.tinymq.core.serializer.Serializer;

/**
 * @author shildon
 */
public class SubscriberFactory {

    public static <T> Subscriber<T> create(Serializer serializer) {
        return new Subscriber<T>(serializer) {
        };
    }

}

package com.shildon.tinymq.client;

import com.shildon.tinymq.core.constant.Constant;
import com.shildon.tinymq.core.model.*;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import io.netty.channel.Channel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Consumer;

/**
 * @author shildon
 */
public class Subscriber<T> {

    private MessageClient messageClient = MessageClient.getInstance();
    private Serializer serializer;
    private Serializer defaultSerializer = new ProtostuffSerializer();
    private Class<T> genericType;
    private RegistryConsumerTable registryConsumerTable = RegistryConsumerTable.getInstance();

    public static <T> Subscriber<T> create(Serializer serializer) {
        return new Subscriber<T>(serializer) {
        };
    }

    private Subscriber(Serializer serializer) {
        this.serializer = serializer;
        this.initGenericType();
    }

    public void subscribe(String topic, Consumer<T> consumer) throws Exception {
        Channel channel = messageClient.borrowChannel();
        try {
            MessageRequestHeader requestHeader = new MessageRequestHeader(System.currentTimeMillis(), Constant.CURRENT_PROTOCOL_VERSION, Operation.SUBSCRIBE);
            SubscribeMessageRequestBody requestBody = new SubscribeMessageRequestBody(topic);
            byte[] serializedData = serializer.serialize(requestBody);
            MessageRequestBody wrappedRequestBody = new MessageRequestBody(serializedData);
            MessageRequest request = new MessageRequest(requestHeader, wrappedRequestBody);
            channel.writeAndFlush(request);
            Consumer<SubscribeMessageResponseBody> wrappedConsumer = responseBody -> {
                byte[] serializedMessage = responseBody.getSerializedMessage();
                T message = serializer.deserialize(serializedMessage, this.genericType);
                consumer.accept(message);
            };
            registryConsumerTable.put(topic, wrappedConsumer);
        } finally {
            messageClient.returnChannel(channel);
        }
    }

    private void initGenericType() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            this.genericType = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        }
    }

}

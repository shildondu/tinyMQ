package com.shildon.tinymq.client.subscriber;

import com.shildon.tinymq.client.MessageClient;
import com.shildon.tinymq.client.RegistryConsumerTable;
import com.shildon.tinymq.core.constant.Constant;
import com.shildon.tinymq.core.model.*;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import io.netty.channel.Channel;

import java.util.function.Consumer;

/**
 * @author shildon
 */
public class Subscriber<T> {

    private MessageClient messageClient = MessageClient.getInstance();
    private RegistryConsumerTable registryConsumerTable = RegistryConsumerTable.getInstance();
    private Serializer defaultSerializer = new ProtostuffSerializer();
    private Serializer serializer;
    private Class<T> genericType;

    Subscriber(Class<T> genericType, Serializer serializer) {
        this.serializer = serializer;
        this.genericType = genericType;
    }

    public void subscribe(String topic, Consumer<T> consumer) throws Exception {
        Channel channel = messageClient.borrowChannel();
        try {
            MessageRequestHeader requestHeader = new MessageRequestHeader(System.currentTimeMillis(), Constant.CURRENT_PROTOCOL_VERSION, Operation.SUBSCRIBE);
            SubscribeMessageRequestBody requestBody = new SubscribeMessageRequestBody(topic);
            byte[] serializedData = this.defaultSerializer.serialize(requestBody);
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

}

package com.shildon.tinymq.client.subscriber;

import com.shildon.tinymq.client.MessageClient;
import com.shildon.tinymq.client.RegistryConsumerTable;
import com.shildon.tinymq.core.protocol.*;
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
            // todo use configuration of group
            SubscribeMessageBody subscribeMessageBody = new SubscribeMessageBody(topic, "");
            byte[] serializedData = this.defaultSerializer.serialize(subscribeMessageBody);
            MessageProtocol request = new MessageProtocol.Builder()
                    .header(
                            new MessageHeader.Builder()
                                    .messageType(MessageType.SUBSCRIBE)
                                    .build()
                    )
                    .body(
                            new MessageBody.Builder()
                                    .serializedData(serializedData)
                                    .build()
                    )
                    .build();
            channel.writeAndFlush(request);
            Consumer<PublishMessageBody> wrappedConsumer = responseBody -> {
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

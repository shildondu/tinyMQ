package com.shildon.tinymq.client.publisher;

import com.shildon.tinymq.client.MessageRetryer;
import com.shildon.tinymq.core.protocol.*;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import com.shildon.tinymq.core.transport.Client;

/**
 * @author shildon
 */
public class Publisher<T> {

    private Client client;
    private Serializer serializer;
    private Serializer defaultSerializer = new ProtostuffSerializer();
    private MessageRetryer messageRetryer;

    Publisher(Client client, Serializer serializer) {
        this.client = client;
        this.serializer = serializer;
        this.messageRetryer = new MessageRetryer(client);
    }

    public void publish(String topic, T message) throws Exception {
        byte[] serializedMessage = this.serializer.serialize(message);
        PublishMessageBody requestBody = new PublishMessageBody(topic, serializedMessage);
        byte[] serializedBody = this.defaultSerializer.serialize(requestBody);
        MessageProtocol request = new MessageProtocol.Builder()
                .header(
                        new MessageHeader.Builder()
                                .messageType(MessageType.PUBLISH)
                                .build()
                )
                .body(
                        new MessageBody.Builder()
                                .serializedData(serializedBody)
                                .build()
                )
                .build();
        try {
            this.client.write(request);
        } finally {
            messageRetryer.retry(request);
        }
    }

}

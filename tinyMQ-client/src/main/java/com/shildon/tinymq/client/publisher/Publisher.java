package com.shildon.tinymq.client.publisher;

import com.shildon.tinymq.client.MessageRetryer;
import com.shildon.tinymq.core.protocol.*;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import com.shildon.tinymq.core.transport.NettyClient;

/**
 * @author shildon
 */
public class Publisher<T> {

    private final NettyClient client;
    private final Serializer serializer;
    private final Serializer defaultSerializer = new ProtostuffSerializer();
    private final MessageRetryer messageRetryer;

    Publisher(final NettyClient client, final Serializer serializer) {
        this.client = client;
        this.serializer = serializer;
        this.messageRetryer = new MessageRetryer(client);
    }

    public void publish(final String topic, final T message) throws Exception {
        final byte[] serializedMessage = this.serializer.serialize(message);
        final PublishMessageBody requestBody = new PublishMessageBody(topic, serializedMessage);
        final byte[] serializedBody = this.defaultSerializer.serialize(requestBody);
        final MessageProtocol request = new MessageProtocol.Builder()
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
            this.messageRetryer.retry(request);
        }
    }

}

package com.shildon.tinymq.client.publisher;

import com.shildon.tinymq.client.MessageCache;
import com.shildon.tinymq.client.MessageClient;
import com.shildon.tinymq.client.MessageRetryer;
import com.shildon.tinymq.core.protocol.*;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import io.netty.channel.Channel;

/**
 * @author shildon
 */
public class Publisher<T> {

    private MessageClient messageClient = MessageClient.getInstance();
    private Serializer serializer;
    private Serializer defaultSerializer = new ProtostuffSerializer();
    private MessageCache messageCache = MessageCache.getInstance();
    private MessageRetryer messageRetryer = MessageRetryer.getInstance();

    Publisher(Serializer serializer) {
        this.serializer = serializer;
    }

    public void publish(String topic, T message) throws Exception {
        Channel channel = messageClient.borrowChannel();
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
            channel.writeAndFlush(request);
        } finally {
            messageClient.returnChannel(channel);
            messageCache.put(request);
            messageRetryer.retry();
        }
    }

}

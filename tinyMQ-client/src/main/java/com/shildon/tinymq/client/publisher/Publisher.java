package com.shildon.tinymq.client.publisher;

import com.shildon.tinymq.client.MessageCache;
import com.shildon.tinymq.client.MessageClient;
import com.shildon.tinymq.core.constant.Constant;
import com.shildon.tinymq.core.model.*;
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

    Publisher(Serializer serializer) {
        this.serializer = serializer;
    }

    public void publish(String topic, T message) throws Exception {
        Channel channel = messageClient.borrowChannel();
        byte[] serializedMessage = this.serializer.serialize(message);
        MessageRequestHeader requestHeader = new MessageRequestHeader(System.currentTimeMillis(), Constant.CURRENT_PROTOCOL_VERSION, Operation.PUBLISH);
        PublishMessageRequestBody requestBody = new PublishMessageRequestBody(topic, serializedMessage);
        byte[] serializedBody = this.defaultSerializer.serialize(requestBody);
        MessageRequestBody wrappedRequestBody = new MessageRequestBody(serializedBody);
        MessageRequest request = new MessageRequest(requestHeader, wrappedRequestBody);
        try {
            channel.writeAndFlush(request);
        } finally {
            messageClient.returnChannel(channel);
            messageCache.put(request);
        }
    }

}

package com.shildon.tinymq.client.subscriber;

import com.shildon.tinymq.client.handler.MessageHandler;
import com.shildon.tinymq.core.codec.MessageFrameDecoder;
import com.shildon.tinymq.core.codec.MessageFrameEncoder;
import com.shildon.tinymq.core.codec.MessageProtocolDecoder;
import com.shildon.tinymq.core.codec.MessageProtocolEncoder;
import com.shildon.tinymq.core.serializer.Serializer;
import com.shildon.tinymq.core.transport.Client;
import com.shildon.tinymq.core.transport.PooledClient;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author shildon
 */
public class SubscriberFactory {

    private static final Client CLIENT;

    static {
        final LoggingHandler loggingHandler = new LoggingHandler();
        CLIENT = new PooledClient.Builder()
                .workerName("message-client-subscriber-worker")
                .channelInitializer(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(final SocketChannel ch) {
                        ch.pipeline()
                                .addLast(loggingHandler)
                                .addLast(new MessageFrameDecoder())
                                .addLast(new MessageFrameEncoder())
                                .addLast(new MessageProtocolDecoder())
                                .addLast(new MessageProtocolEncoder())
                                .addLast(new MessageHandler());
                    }
                })
                .build();
    }

    public static <T> Subscriber<T> create(Class<T> type, Serializer serializer) {
        return new Subscriber<>(CLIENT, type, serializer);
    }

    public static void destroy() {
        CLIENT.close();
    }

}

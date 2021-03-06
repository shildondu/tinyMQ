package com.shildon.tinymq.client.publisher;

import com.shildon.tinymq.client.handler.MessageHandler;
import com.shildon.tinymq.core.codec.MessageFrameDecoder;
import com.shildon.tinymq.core.codec.MessageFrameEncoder;
import com.shildon.tinymq.core.codec.MessageProtocolDecoder;
import com.shildon.tinymq.core.codec.MessageProtocolEncoder;
import com.shildon.tinymq.core.serializer.Serializer;
import com.shildon.tinymq.core.transport.NettyClient;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * publisher工厂
 *
 * @author shildon
 */
public class PublisherFactory {

    private static final NettyClient CLIENT;

    static {
        final LoggingHandler loggingHandler = new LoggingHandler();
        CLIENT = new NettyClient.Builder()
                .serverHost("127.0.0.1")
                .serverPort(10101)
                .workerName("message-client-publisher-worker")
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

    public static <T> Publisher<T> create(Serializer serializer) {
        return new Publisher<>(CLIENT, serializer);
    }

    public static void destroy() {
        CLIENT.close();
    }

}

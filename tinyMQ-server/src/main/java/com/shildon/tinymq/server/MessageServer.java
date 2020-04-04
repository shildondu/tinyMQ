package com.shildon.tinymq.server;

import com.shildon.tinymq.core.codec.MessageFrameDecoder;
import com.shildon.tinymq.core.codec.MessageFrameEncoder;
import com.shildon.tinymq.core.codec.MessageProtocolDecoder;
import com.shildon.tinymq.core.codec.MessageProtocolEncoder;
import com.shildon.tinymq.core.transport.Server;
import com.shildon.tinymq.server.handler.MessageHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器端
 *
 * @author shildon
 */
public class MessageServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServer.class);

    public static void main(String[] args) {
        LoggingHandler loggingHandler = new LoggingHandler();
        new Server.Builder()
                .port(10101)
                .bossName("message-server-boss")
                .workerName("message-server-worker")
                .handler(new ChannelInitializer<ServerSocketChannel>() {
                    @Override
                    protected void initChannel(ServerSocketChannel ch) {
                        ch.pipeline()
                                .addLast(loggingHandler);
                    }
                })
                .childHandler(new ChannelInitializer<SocketChannel>() {
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
                .build()
                .run();
    }

}

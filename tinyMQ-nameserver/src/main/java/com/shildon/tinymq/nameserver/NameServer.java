package com.shildon.tinymq.nameserver;

import com.shildon.tinymq.core.codec.MessageFrameDecoder;
import com.shildon.tinymq.core.codec.MessageFrameEncoder;
import com.shildon.tinymq.core.codec.MessageProtocolDecoder;
import com.shildon.tinymq.core.codec.MessageProtocolEncoder;
import com.shildon.tinymq.core.processor.MessageHandler;
import com.shildon.tinymq.core.processor.MessageProcessor;
import com.shildon.tinymq.core.transport.Server;
import com.shildon.tinymq.nameserver.processor.RegisterServerProcessor;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 寻址服务器
 *
 * @author shildon
 */
public class NameServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NameServer.class);

    public static void main(String[] args) {
        final List<MessageProcessor> messageProcessors = new ArrayList<MessageProcessor>();
        messageProcessors.add(new RegisterServerProcessor());
        final LoggingHandler loggingHandler = new LoggingHandler();
        new Server.Builder()
                .port(20202)
                .bossName("name-server-boss")
                .workerName("name-server-worker")
                .handler(new ChannelInitializer<ServerSocketChannel>() {
                    @Override
                    protected void initChannel(ServerSocketChannel ch) {
                        ch.pipeline()
                                .addLast(loggingHandler);
                    }
                })
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(loggingHandler)
                                .addLast(new MessageFrameDecoder())
                                .addLast(new MessageFrameEncoder())
                                .addLast(new MessageProtocolDecoder())
                                .addLast(new MessageProtocolEncoder())
                                .addLast(new MessageHandler(messageProcessors));
                    }
                })
                .build()
                .run();
    }

}

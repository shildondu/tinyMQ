package com.shildon.tinymq.nameserver;

import com.shildon.tinymq.core.codec.MessageFrameDecoder;
import com.shildon.tinymq.core.codec.MessageFrameEncoder;
import com.shildon.tinymq.core.codec.MessageProtocolDecoder;
import com.shildon.tinymq.core.codec.MessageProtocolEncoder;
import com.shildon.tinymq.core.transport.Server;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 寻址服务器
 *
 * @author shildon
 */
public class NameServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NameServer.class);

    public static void main(String[] args) {
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
                                .addLast(new MessageFrameDecoder())
                                .addLast(new MessageFrameEncoder())
                                .addLast(new MessageProtocolDecoder())
                                .addLast(new MessageProtocolEncoder());
                    }
                })
                .build()
                .run();
    }

}

package com.shildon.tinymq.server;

import com.shildon.tinymq.core.codec.MessageFrameDecoder;
import com.shildon.tinymq.core.codec.MessageFrameEncoder;
import com.shildon.tinymq.core.codec.MessageProtocolDecoder;
import com.shildon.tinymq.core.codec.MessageProtocolEncoder;
import com.shildon.tinymq.server.handler.MessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器端
 *
 * @author shildon
 */
public class MessageServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServer.class);

    private final int port;

    public MessageServer(final int port) {
        this.port = port;
    }

    public void run() {
        final EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
        final EventLoopGroup workerGroup = new NioEventLoopGroup(new DefaultThreadFactory("workers"));
        final LoggingHandler loggingHandler = new LoggingHandler();
        try {
            final ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(loggingHandler)
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
                    // set connections queue size
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // no delay for sending packet
                    .childOption(ChannelOption.TCP_NODELAY, true);

            final ChannelFuture channelFuture = serverBootstrap.bind(this.port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error("start message server error!", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        MessageServer messageServer = new MessageServer(10101);
        messageServer.run();
    }

}

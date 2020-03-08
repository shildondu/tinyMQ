package com.shildon.tinymq.client;

import com.shildon.tinymq.client.codec.MessageRequestEncoder;
import com.shildon.tinymq.client.codec.MessageResponseDecoder;
import com.shildon.tinymq.core.codec.MessageFrameDecoder;
import com.shildon.tinymq.core.codec.MessageFrameEncoder;
import com.shildon.tinymq.core.model.MessageRequest;
import com.shildon.tinymq.core.model.MessageRequestBody;
import com.shildon.tinymq.core.model.MessageRequestHeader;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shildon
 */
public class MessageClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageClient.class);

    private String host;
    private int port;

    private EventLoopGroup workers;
    private Channel channel;

    public MessageClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.init();
    }

    public void init() {
        final EventLoopGroup workers = new NioEventLoopGroup(new DefaultThreadFactory("workers"));
        this.workers = workers;
        final LoggingHandler loggingHandler = new LoggingHandler();
        try {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workers)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(final SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new MessageFrameDecoder())
                                    .addLast(new MessageFrameEncoder())
                                    .addLast(new MessageResponseDecoder())
                                    .addLast(new MessageRequestEncoder())
                                    .addLast(loggingHandler);
                        }
                    })
                    .option(ChannelOption.TCP_NODELAY, true);

            final ChannelFuture channelFuture = bootstrap.connect(this.host, this.port).sync();
            this.channel = channelFuture.channel();
        } catch (InterruptedException e) {
            LOGGER.error("connect to message server error!", e);
        }
    }

    public void send(MessageRequest messageRequest) throws InterruptedException {
        this.channel.writeAndFlush(messageRequest).sync();
    }

    public void close() throws InterruptedException {
        try {
            this.channel.close().sync();
        } finally {
            this.workers.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MessageClient messageClient = new MessageClient("127.0.0.1", 10101);
        messageClient.send(new MessageRequest(new MessageRequestHeader(1, 1, 1), new MessageRequestBody("test".getBytes())));
        Thread.sleep(3000L);
        messageClient.close();
    }

}

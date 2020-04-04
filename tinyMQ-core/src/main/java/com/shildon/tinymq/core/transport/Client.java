package com.shildon.tinymq.core.transport;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shildon
 */
public final class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private Bootstrap bootstrap;
    private EventLoopGroup workers;

    private Client(Builder builder) {
        if (builder.workerSize > 0) {
            this.workers = new NioEventLoopGroup(builder.workerSize, new DefaultThreadFactory(builder.workerName));
        } else {
            this.workers = new NioEventLoopGroup(new DefaultThreadFactory(builder.workerName));
        }
        this.bootstrap = new Bootstrap()
                .group(this.workers)
                .channel(NioSocketChannel.class)
                .handler(builder.channelInitializer)
                .option(ChannelOption.TCP_NODELAY, true);
    }

    public Channel connect(String host, int port) throws InterruptedException {
        try {
            return this.bootstrap.connect(host, port).sync().channel();
        } catch (InterruptedException e) {
            LOGGER.error("connect to {}:{} error!", host, port, e);
            throw e;
        }
    }

    public void close() {
        this.workers.shutdownGracefully();
    }

    public static final class Builder {
        private int workerSize;
        private String workerName;
        private ChannelInitializer<SocketChannel> channelInitializer;

        public Builder workerSize(int workerSize) {
            this.workerSize = workerSize;
            return this;
        }

        public Builder workerName(String workerName) {
            this.workerName = workerName;
            return this;
        }

        public Builder channelInitializer(ChannelInitializer<SocketChannel> channelInitializer) {
            this.channelInitializer = channelInitializer;
            return this;
        }

        public Client build() {
            return new Client(this);
        }
    }

}

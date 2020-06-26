package com.shildon.tinymq.core.transport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shildon
 */
public final class NettyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    private int port;
    private ServerBootstrap serverBootstrap;
    private EventLoopGroup boss;
    private EventLoopGroup workers;
    private GenericFutureListener<? extends Future<? super Void>> afterBindListener;

    private NettyServer(Builder builder) {
        this.port = builder.port;
        this.boss = new NioEventLoopGroup(builder.bossSize, new DefaultThreadFactory(builder.bossName));
        if (builder.workerSize > 0) {
            this.workers = new NioEventLoopGroup(builder.workerSize, new DefaultThreadFactory(builder.workerName));
        } else {
            this.workers = new NioEventLoopGroup(new DefaultThreadFactory(builder.workerName));
        }
        this.serverBootstrap = new ServerBootstrap()
                .group(this.boss, this.workers)
                .channel(NioServerSocketChannel.class)
                .handler(builder.channelInitializer)
                .childHandler(builder.childChannelInitializer)
                // set connections queue size
                .option(ChannelOption.SO_BACKLOG, 1024)
                // no delay for sending packet
                .childOption(ChannelOption.TCP_NODELAY, true);
        this.afterBindListener = builder.afterBindListener;
    }

    public void run() {
        try {
            final ChannelFuture channelFuture = this.serverBootstrap
                    .bind(this.port);
            if (afterBindListener != null) {
                channelFuture.addListener(this.afterBindListener);
            }
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error("start message server error!", e);
        } finally {
            this.boss.shutdownGracefully();
            this.workers.shutdownGracefully();
        }
    }

    public static final class Builder {
        private int port;
        private int bossSize = 1;
        private int workerSize;
        private String bossName;
        private String workerName;
        private ChannelInitializer<ServerSocketChannel> channelInitializer;
        private ChannelInitializer<SocketChannel> childChannelInitializer;
        private GenericFutureListener<? extends Future<? super Void>> afterBindListener;

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder bossSize(int bossSize) {
            this.bossSize = bossSize;
            return this;
        }

        public Builder workerSize(int workerSize) {
            this.workerSize = workerSize;
            return this;
        }

        public Builder bossName(String bossName) {
            this.bossName = bossName;
            return this;
        }

        public Builder workerName(String workerName) {
            this.workerName = workerName;
            return this;
        }

        public Builder handler(ChannelInitializer<ServerSocketChannel> channelInitializer) {
            this.channelInitializer = channelInitializer;
            return this;
        }

        public Builder childHandler(ChannelInitializer<SocketChannel> childChannelInitializer) {
            this.childChannelInitializer = childChannelInitializer;
            return this;
        }

        public Builder afterBindListener(GenericFutureListener<? extends Future<? super Void>> afterBindListener) {
            this.afterBindListener = afterBindListener;
            return this;
        }

        public NettyServer build() {
            return new NettyServer(this);
        }
    }

}

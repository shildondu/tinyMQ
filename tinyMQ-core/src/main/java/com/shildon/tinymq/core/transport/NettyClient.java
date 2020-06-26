package com.shildon.tinymq.core.transport;

import com.shildon.tinymq.core.pool.ChannelFactory;
import com.shildon.tinymq.core.protocol.MessageProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 池化tcp连接客户端
 *
 * @author shildon
 */
public final class NettyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    private EventLoopGroup workers;
    private ChannelFactory channelFactory;
    private ObjectPool<Channel> channelPool;
    private Bootstrap bootstrap;

    private NettyClient(Builder builder) {
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
        this.channelFactory = new ChannelFactory(this.bootstrap, builder.serverHost, builder.serverPort);
        this.channelPool = this.initChannelPool(builder.maxChannelSize);
    }

    private ObjectPool<Channel> initChannelPool(int maxChannelSize) {
        GenericObjectPoolConfig<Channel> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(maxChannelSize);
        return new GenericObjectPool<>(this.channelFactory, poolConfig);
    }

    public Channel connect(String host, int port) throws InterruptedException {
        return this.bootstrap.connect(host, port).sync().channel();
    }

    public void write(MessageProtocol messageProtocol) throws Exception {
        Channel channel = this.channelPool.borrowObject();
        try {
            channel.writeAndFlush(messageProtocol);
        } finally {
            this.channelPool.returnObject(channel);
        }
    }

    public void close() {
        this.channelPool.close();
        this.workers.shutdownGracefully();
    }

    public static final class Builder {
        private int workerSize;
        private String workerName;
        private ChannelInitializer<SocketChannel> channelInitializer;
        private String serverHost;
        private int serverPort;
        private int maxChannelSize = 1;

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

        public Builder serverHost(String serverHost) {
            this.serverHost = serverHost;
            return this;
        }

        public Builder serverPort(int serverPort) {
            this.serverPort = serverPort;
            return this;
        }

        public Builder maxChannelSize(int maxChannelSize) {
            this.maxChannelSize = maxChannelSize;
            return this;
        }

        public NettyClient build() {
            return new NettyClient(this);
        }
    }

}

package com.shildon.tinymq.client;

import com.shildon.tinymq.client.handler.MessageHandler;
import com.shildon.tinymq.client.pool.ChannelFactory;
import com.shildon.tinymq.core.codec.MessageFrameDecoder;
import com.shildon.tinymq.core.codec.MessageFrameEncoder;
import com.shildon.tinymq.core.codec.MessageProtocolDecoder;
import com.shildon.tinymq.core.codec.MessageProtocolEncoder;
import com.shildon.tinymq.core.transport.Client;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shildon
 */
public final class MessageClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageClient.class);
    private static MessageClient INSTANCE = new MessageClient();

    public static MessageClient getInstance() {
        return INSTANCE;
    }

    private Client client;
    private Configuration configuration;
    private ChannelFactory channelFactory;
    private ObjectPool<Channel> channelPool;

    private MessageClient() {
        final LoggingHandler loggingHandler = new LoggingHandler();
        this.client = new Client.Builder()
                .workerName("message-client-worker")
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
        this.configuration = this.initConfiguration();
        this.channelFactory = new ChannelFactory(this.client, this.configuration);
        this.channelPool = this.initChannelPool();
    }

    private ObjectPool<Channel> initChannelPool() {
        // todo use configuration
        GenericObjectPoolConfig<Channel> poolConfig = new GenericObjectPoolConfig<>();
        int defaultEventLoopThreads = Math.max(1, SystemPropertyUtil.getInt(
                "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
        poolConfig.setMaxTotal(defaultEventLoopThreads);
        return new GenericObjectPool<>(this.channelFactory, poolConfig);
    }

    private Configuration initConfiguration() {
        // todo add more configurations
        Configuration configuration = new Configuration();
        configuration.setHost("127.0.0.1");
        configuration.setPort(10101);
        LOGGER.info("the configuration:\n{}", configuration);
        return configuration;
    }

    public Channel borrowChannel() throws Exception {
        return this.channelPool.borrowObject();
    }

    public void returnChannel(Channel channel) throws Exception {
        this.channelPool.returnObject(channel);
    }

    public void close() {
        this.channelPool.close();
        this.client.close();
    }

}

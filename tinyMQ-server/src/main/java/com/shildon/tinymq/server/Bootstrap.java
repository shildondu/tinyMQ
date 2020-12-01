package com.shildon.tinymq.server;

import com.shildon.tinymq.core.codec.MessageFrameDecoder;
import com.shildon.tinymq.core.codec.MessageFrameEncoder;
import com.shildon.tinymq.core.codec.MessageProtocolDecoder;
import com.shildon.tinymq.core.codec.MessageProtocolEncoder;
import com.shildon.tinymq.core.model.meta.ServerInfo;
import com.shildon.tinymq.core.protocol.MessageBody;
import com.shildon.tinymq.core.protocol.MessageHeader;
import com.shildon.tinymq.core.protocol.MessageProtocol;
import com.shildon.tinymq.core.protocol.MessageType;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import com.shildon.tinymq.core.transport.NettyClient;
import com.shildon.tinymq.core.transport.NettyServer;
import com.shildon.tinymq.server.configuration.ConfigurationHolder;
import com.shildon.tinymq.server.handler.MessageHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 服务器端
 *
 * @author shildon
 */
public final class Bootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    private ConfigurationHolder configurationHolder = ConfigurationHolder.getInstance();
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("register-scheduler"));

    private Bootstrap() {

    }

    public static void main(String[] args) {
        new Bootstrap()
                .runServer();
    }

    private void runServer() {
        LoggingHandler loggingHandler = new LoggingHandler();
        new NettyServer.Builder()
                .port(this.configurationHolder.getPort())
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
                // .afterBindListener(it -> registerToNameServer())
                .build()
                .run();
    }

    private void registerToNameServer() {
        Serializer serializer = new ProtostuffSerializer();
        LoggingHandler loggingHandler = new LoggingHandler();
        NettyClient client = new NettyClient.Builder()
                .workerSize(1)
                .workerName("register-worker")
                .channelInitializer(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(loggingHandler)
                                .addLast(new MessageFrameDecoder())
                                .addLast(new MessageFrameEncoder())
                                .addLast(new MessageProtocolDecoder())
                                .addLast(new MessageProtocolEncoder());
                    }
                })
                .build();

        List<Channel> channels = this.configurationHolder.getNameServerInfos()
                .stream()
                .map(it -> {
                    try {
                        return client.connect(it.getFirst(), it.getSecond());
                    } catch (InterruptedException e) {
                        LOGGER.error("connect to {}:{} failed!", it.getFirst(), it.getSecond(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        this.scheduler.scheduleWithFixedDelay(() ->
                channels.forEach(it ->
                        it.writeAndFlush(
                                new MessageProtocol.Builder()
                                        .header(
                                                new MessageHeader.Builder()
                                                        .messageType(MessageType.REGISTER_SERVER)
                                                        .build()
                                        )
                                        .body(
                                                new MessageBody.Builder()
                                                        .serializedData(serializer.serialize(
                                                                new ServerInfo("localhost", this.configurationHolder.getPort())
                                                        ))
                                                        .build()
                                        )
                                        .build()
                        )), 3, 1, TimeUnit.MINUTES);
    }

}

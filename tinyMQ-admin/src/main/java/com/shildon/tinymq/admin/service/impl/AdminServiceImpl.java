package com.shildon.tinymq.admin.service.impl;

import com.shildon.tinymq.admin.configuration.ConfigurationHolder;
import com.shildon.tinymq.admin.service.AdminService;
import com.shildon.tinymq.core.codec.MessageFrameDecoder;
import com.shildon.tinymq.core.codec.MessageFrameEncoder;
import com.shildon.tinymq.core.codec.MessageProtocolDecoder;
import com.shildon.tinymq.core.codec.MessageProtocolEncoder;
import com.shildon.tinymq.core.model.Pair;
import com.shildon.tinymq.core.protocol.MessageBody;
import com.shildon.tinymq.core.protocol.MessageHeader;
import com.shildon.tinymq.core.protocol.MessageProtocol;
import com.shildon.tinymq.core.protocol.MessageType;
import com.shildon.tinymq.core.transport.NettyClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shildon
 */
public class AdminServiceImpl implements AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);
    private ConfigurationHolder configurationHolder = ConfigurationHolder.getInstance();

    public void createTopic(String topicName, int queueSize) {
        // call name server to get server infos
        // allocate queue to server
        // call message server to create corresponding queue
    }

    private void getServerInfos() {
        final LoggingHandler loggingHandler = new LoggingHandler();
        NettyClient client = new NettyClient.Builder()
                .workerName("admin-worker")
                .workerSize(1)
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
        Pair<String, Integer> serverInfo = this.configurationHolder.getNameServerInfos().get(0);
        try {
            Channel channel = client.connect(serverInfo.getFirst(), serverInfo.getSecond());
            MessageProtocol request = new MessageProtocol.Builder()
                    .header(
                            new MessageHeader.Builder()
                                    .messageType(MessageType.GET_SERVER_LIST)
                                    .build()
                    )
                    .body(
                            new MessageBody.Builder()
                                    .build()
                    )
                    .build();
            channel.writeAndFlush(request);
        } catch (InterruptedException e) {
            LOGGER.error("connect to name server failed! host: {}, port: {}", serverInfo.getFirst(), serverInfo.getSecond());
        }
    }

}

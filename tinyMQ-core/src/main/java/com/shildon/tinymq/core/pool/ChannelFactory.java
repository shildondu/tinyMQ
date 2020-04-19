package com.shildon.tinymq.core.pool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shildon
 */
public class ChannelFactory implements PooledObjectFactory<Channel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelFactory.class);

    private Bootstrap bootstrap;
    private String serverHost;
    private int serverPort;

    public ChannelFactory(Bootstrap bootstrap, String serverHost, int serverPort) {
        this.bootstrap = bootstrap;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    public PooledObject<Channel> makeObject() throws Exception {
        Channel channel = this.bootstrap.connect(serverHost, serverPort).sync().channel();
        LOGGER.info("connect to server and generate a channel [{}]", channel);
        return new DefaultPooledObject<>(channel);
    }

    @Override
    public void destroyObject(PooledObject<Channel> pooledObject) throws Exception {
        Channel channel = pooledObject.getObject();
        LOGGER.info("destroy the channel [{}]", channel);
        channel.close().sync();
    }

    @Override
    public boolean validateObject(PooledObject<Channel> pooledObject) {
        Channel channel = pooledObject.getObject();
        LOGGER.info("validate the channel [{}]", channel);
        return channel.isActive();
    }

    @Override
    public void activateObject(PooledObject<Channel> pooledObject) {
        Channel channel = pooledObject.getObject();
        LOGGER.info("activate channel [{}]", channel);
    }

    @Override
    public void passivateObject(PooledObject<Channel> pooledObject) {
        Channel channel = pooledObject.getObject();
        LOGGER.info("passivate the channel [{}]", channel);
    }

}

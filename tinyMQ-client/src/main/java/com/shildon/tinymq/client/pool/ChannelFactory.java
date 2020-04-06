package com.shildon.tinymq.client.pool;

import com.shildon.tinymq.client.MessageClient;
import com.shildon.tinymq.client.configuration.Configuration;
import com.shildon.tinymq.client.configuration.ConfigurationHolder;
import com.shildon.tinymq.core.transport.Client;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageClient.class);

    private Client client;
    private ConfigurationHolder configurationHolder = ConfigurationHolder.getInstance();

    public ChannelFactory(Client client) {
        this.client = client;
    }

    @Override
    public PooledObject<Channel> makeObject() throws Exception {
        Channel channel = this.client.connect(configurationHolder.getServerInfos().get(0).getFirst(), configurationHolder.getServerInfos().get(0).getSecond());
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

package com.shildon.tinymq.server;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shildon
 */
public final class RegistryChannelTable {

    private static final RegistryChannelTable INSTANCE = new RegistryChannelTable();

    private Map<String, List<Channel>> registryMap = new ConcurrentHashMap<>();

    private RegistryChannelTable() {
    }

    public static RegistryChannelTable getInstance() {
        return INSTANCE;
    }

    public void put(String topic, Channel channel) {
        this.registryMap.compute(topic, (key, channels) -> {
            if (channels == null) {
                List<Channel> newChannels = new ArrayList<>();
                newChannels.add(channel);
                return newChannels;
            } else {
                channels.add(channel);
                return channels;
            }
        });
    }

    public List<Channel> get(String topic) {
        return this.registryMap.getOrDefault(topic, new ArrayList<>());
    }

}

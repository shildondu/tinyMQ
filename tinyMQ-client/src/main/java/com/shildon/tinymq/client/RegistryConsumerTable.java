package com.shildon.tinymq.client;

import com.shildon.tinymq.core.protocol.PublishMessageBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author shildon
 */
public final class RegistryConsumerTable {

    private static final RegistryConsumerTable INSTANCE = new RegistryConsumerTable();

    private Map<String, List<Consumer<PublishMessageBody>>> registryMap = new ConcurrentHashMap<>();

    private RegistryConsumerTable() {
    }

    public static RegistryConsumerTable getInstance() {
        return INSTANCE;
    }

    public <T> void put(String topic, Consumer<PublishMessageBody> consumer) {
        this.registryMap.compute(topic, (key, responseConsumers) -> {
            if (responseConsumers == null) {
                List<Consumer<PublishMessageBody>> newResponseConsumers = new ArrayList<>();
                newResponseConsumers.add(consumer);
                return newResponseConsumers;
            } else {
                responseConsumers.add(consumer);
                return responseConsumers;
            }
        });
    }

    public List<Consumer<PublishMessageBody>> get(String topic) {
        return this.registryMap.getOrDefault(topic, new ArrayList<>());
    }

}

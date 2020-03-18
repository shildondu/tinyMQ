package com.shildon.tinymq.client;

import com.shildon.tinymq.core.model.SubscribeMessageResponseBody;

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

    private Map<String, List<Consumer<SubscribeMessageResponseBody>>> registryMap = new ConcurrentHashMap<>();

    private RegistryConsumerTable() {
    }

    public static RegistryConsumerTable getInstance() {
        return INSTANCE;
    }

    public <T> void put(String topic, Consumer<SubscribeMessageResponseBody> consumer) {
        this.registryMap.compute(topic, (key, responseConsumers) -> {
            if (responseConsumers == null) {
                List<Consumer<SubscribeMessageResponseBody>> newResponseConsumers = new ArrayList<>();
                newResponseConsumers.add(consumer);
                return newResponseConsumers;
            } else {
                responseConsumers.add(consumer);
                return responseConsumers;
            }
        });
    }

    public List<Consumer<SubscribeMessageResponseBody>> get(String topic) {
        return this.registryMap.getOrDefault(topic, new ArrayList<>());
    }

}

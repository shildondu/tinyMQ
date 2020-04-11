package com.shildon.tinymq.nameserver.registry.impl;

import com.shildon.tinymq.core.model.meta.ServerInfo;
import com.shildon.tinymq.nameserver.registry.ServerInfoRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shildon
 */
public class ServerInfoRegistryImpl implements ServerInfoRegistry {

    private final Map<String, ServerInfo> serverInfoMap = new ConcurrentHashMap<>();

    public List<ServerInfo> getAll() {
        return new ArrayList<>(serverInfoMap.values());
    }

    public void register(ServerInfo serverInfo) {
        serverInfoMap.put(serverInfo.getIp(), serverInfo);
    }

}

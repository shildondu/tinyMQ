package com.shildon.tinymq.nameserver.registry.impl;

import com.shildon.tinymq.core.model.meta.ServerInfo;
import com.shildon.tinymq.nameserver.registry.ServerInfoRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author shildon
 */
public class ServerInfoRegistryImpl implements ServerInfoRegistry {

    private final Map<String, ServerInfo> serverInfoMap = new ConcurrentHashMap<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public List<ServerInfo> getAll() {
        lock.readLock().lock();
        List<ServerInfo> serverInfos;
        try {
            serverInfos = new ArrayList<>(serverInfoMap.values());
        } finally {
            lock.readLock().unlock();
        }
        return serverInfos;
    }

    public void register(ServerInfo serverInfo) {
        lock.writeLock().lock();
        try {
            serverInfoMap.put(serverInfo.getIp(), serverInfo);
        } finally {
            lock.writeLock().unlock();
        }
    }

}

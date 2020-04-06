package com.shildon.tinymq.nameserver.registry.impl;

import com.shildon.tinymq.core.model.meta.ServerInfo;
import com.shildon.tinymq.nameserver.registry.ServerInfoRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author shildon
 */
public class ServerInfoRegistryImpl implements ServerInfoRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerInfoRegistryImpl.class);

    public List<ServerInfo> getAll() {
        return null;
    }

    public void register(ServerInfo serverInfo) {
        LOGGER.info("" + serverInfo);
    }

}

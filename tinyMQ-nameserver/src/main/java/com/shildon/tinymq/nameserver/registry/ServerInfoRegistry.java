package com.shildon.tinymq.nameserver.registry;

import com.shildon.tinymq.core.model.meta.ServerInfo;

import java.util.List;

/**
 * @author shildon
 */
public interface ServerInfoRegistry {

    List<ServerInfo> getAll();

    void register(ServerInfo serverInfo);

}

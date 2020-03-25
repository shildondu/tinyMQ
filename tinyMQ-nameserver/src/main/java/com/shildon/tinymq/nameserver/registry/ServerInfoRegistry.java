package com.shildon.tinymq.nameserver.registry;

import com.shildon.tinymq.nameserver.model.ServerInfo;

import java.util.List;

/**
 * @author shildon
 */
public interface ServerInfoRegistry {

    List<ServerInfo> getAll();

    void register(ServerInfo serverInfo);

}

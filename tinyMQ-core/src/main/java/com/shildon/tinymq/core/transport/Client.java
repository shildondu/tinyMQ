package com.shildon.tinymq.core.transport;

import com.shildon.tinymq.core.protocol.MessageProtocol;

/**
 * tcp连接客户端
 *
 * @author shildon
 */
public interface Client {

    void write(MessageProtocol messageProtocol) throws Exception;

    void close();

}

package com.shildon.tinymq.nameserver.processor;

import com.shildon.tinymq.core.processor.MessageProcessor;
import com.shildon.tinymq.core.protocol.MessageProtocol;
import com.shildon.tinymq.core.protocol.MessageType;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import com.shildon.tinymq.nameserver.model.ServerInfo;
import com.shildon.tinymq.nameserver.registry.ServerInfoRegistry;
import com.shildon.tinymq.nameserver.registry.impl.ServerInfoRegistryImpl;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author shildon
 */
public class RegisterServerProcessor implements MessageProcessor {

    private Serializer serializer = new ProtostuffSerializer();
    private ServerInfoRegistry serverInfoRegistry = new ServerInfoRegistryImpl();

    public void process(ChannelHandlerContext context, MessageProtocol messageProtocol) {
        ServerInfo serverInfo = this.serializer.deserialize(messageProtocol.getBody().getSerializedData(), ServerInfo.class);
        this.serverInfoRegistry.register(serverInfo);
    }

    public MessageType supportedMessageType() {
        return MessageType.REGISTER_SERVER;
    }

}

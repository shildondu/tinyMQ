package com.shildon.tinymq.nameserver.processor;

import com.shildon.tinymq.core.model.meta.ServerInfo;
import com.shildon.tinymq.core.processor.MessageProcessor;
import com.shildon.tinymq.core.protocol.MessageProtocol;
import com.shildon.tinymq.core.protocol.MessageType;
import com.shildon.tinymq.core.serializer.ProtostuffSerializer;
import com.shildon.tinymq.core.serializer.Serializer;
import com.shildon.tinymq.nameserver.registry.ServerInfoRegistry;
import com.shildon.tinymq.nameserver.registry.impl.ServerInfoRegistryImpl;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shildon
 */
public class RegisterServerProcessor implements MessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterServerProcessor.class);

    private Serializer serializer = new ProtostuffSerializer();
    private ServerInfoRegistry serverInfoRegistry = new ServerInfoRegistryImpl();

    public void process(ChannelHandlerContext context, MessageProtocol messageProtocol) {
        ServerInfo serverInfo = this.serializer.deserialize(messageProtocol.getBody().getSerializedData(), ServerInfo.class);
        LOGGER.info("register server info: {}", serverInfo);
        this.serverInfoRegistry.register(serverInfo);
    }

    public MessageType supportedMessageType() {
        return MessageType.REGISTER_SERVER;
    }

}

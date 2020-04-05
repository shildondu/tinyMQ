package com.shildon.tinymq.core.processor;

import com.shildon.tinymq.core.protocol.MessageProtocol;
import com.shildon.tinymq.core.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shildon
 */
public class MessageHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private Map<MessageType, MessageProcessor> messageProcessorMap;

    public MessageHandler(List<MessageProcessor> messageProcessors) {
        this.messageProcessorMap = messageProcessors.stream()
                .collect(Collectors.toMap(MessageProcessor::supportedMessageType, messageProcessor -> messageProcessor));
    }

    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol messageProtocol) {
        LOGGER.info("start handle request -> {}", messageProtocol);
        int type = messageProtocol.getHeader().getMessageType();
        MessageType messageType = MessageType.find(type);
        messageProcessorMap.get(messageType)
                .process(ctx, messageProtocol);
    }

}

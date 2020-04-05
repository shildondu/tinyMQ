package com.shildon.tinymq.core.processor;

import com.shildon.tinymq.core.protocol.MessageProtocol;
import com.shildon.tinymq.core.protocol.MessageType;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author shildon
 */
public interface MessageProcessor {

    void process(ChannelHandlerContext context, MessageProtocol messageProtocol);

    MessageType supportedMessageType();

}

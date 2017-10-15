package com.shildon.tinyMQ.client.handler;

import com.shildon.tinyMQ.core.MQRequest;
import com.shildon.tinyMQ.util.SerializeUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端编码器。
 * @author shildon<shildondu@gmail.com>
 * @date May 6, 2016
 */
public class MQClientEncoder extends MessageToByteEncoder<MQRequest> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MQClientEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, MQRequest msg, ByteBuf out) throws Exception {
		LOGGER.debug("enter mq client encode ...");
		byte[] data = SerializeUtil.serialize(msg);
		out.writeBytes(data);
	}

}

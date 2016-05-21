package com.shildon.tinyMQ.server.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shildon.tinyMQ.core.MQResponse;
import com.shildon.tinyMQ.util.SerializeUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 服务器端编码器。
 * @author shildon<shildondu@gmail.com>
 * @date May 3, 2016
 */
public class MQServerEncoder extends MessageToByteEncoder<MQResponse> {
	
	private final Log log = LogFactory.getLog(MQServerEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, MQResponse msg, ByteBuf out) throws Exception {
		log.debug("enter mq server encode ...");
		byte[] data = SerializeUtil.serialize(msg);
		out.writeBytes(data);
	}

}

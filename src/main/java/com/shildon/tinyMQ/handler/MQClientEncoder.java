package com.shildon.tinyMQ.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shildon.tinyMQ.core.MQRequest;
import com.shildon.tinyMQ.util.SerializeUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 客户端编码器。
 * @author shildon<shildondu@gmail.com>
 * @date May 6, 2016
 */
public class MQClientEncoder extends MessageToByteEncoder<MQRequest> {
	
	private final Log log = LogFactory.getLog(MQClientEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, MQRequest msg, ByteBuf out) throws Exception {
		log.debug("enter mq client encode ...");
		byte[] data = SerializeUtil.serialize(msg);
		out.writeBytes(data);
	}

}

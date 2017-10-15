package com.shildon.tinyMQ.server.handler;

import java.util.List;

import com.shildon.tinyMQ.util.SerializeUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器端解码器。
 * @author shildon<shildondu@gmail.com>
 * @date May 3, 2016
 */
public class MQServerDecoder extends ByteToMessageDecoder {
	
	private Class<?> clazz;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MQServerDecoder.class);
	
	public MQServerDecoder(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		LOGGER.debug("enter mq server decode ...");
		int size = in.readableBytes();
		byte[] data = new byte[size];
		in.readBytes(data);
		Object object;
		
		try {
			object = SerializeUtil.deserialize(data, clazz);
		} catch(IllegalStateException e) {
			LOGGER.error("the request is not a mqrequest!");
			// 关闭SocketChannel
			ctx.close().sync();
			return;
		}
		
		out.add(object);
	}

}

package com.shildon.tinyMQ.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shildon.tinyMQ.core.MQRequest;
import com.shildon.tinyMQ.core.MQResponse;
import com.shildon.tinyMQ.storage.RedisTemplate;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 服务器端处理器。
 * @author shildon<shildondu@gmail.com>
 * @date May 6, 2016
 */
public class MQServerHandler extends SimpleChannelInboundHandler<MQRequest> {
	
	// TODO
	private RedisTemplate redisTemplate = RedisTemplate.getInstance();

	private final Log log = LogFactory.getLog(MQServerHandler.class);

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, MQRequest msg) throws Exception {
		log.debug("enter server handler ...");
		log.debug("mq request type: " + msg.getMqTransferType());
		if (null != msg.getMqTransferType()) {
			MQResponse mqResponse = handle(msg);
			ctx.writeAndFlush(mqResponse).addListener(ChannelFutureListener.CLOSE);
		} else {
			log.debug("mq request is null!");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("server handler error!", cause);
		ctx.close();
	}
	
	private MQResponse handle(MQRequest mqRequest) {
		MQResponse mqResponse = new MQResponse()
				.setMqTransferType(mqRequest.getMqTransferType());
		
		String queueId = null;
		Object content = null;
		
		switch (mqRequest.getMqTransferType()) {
		case offer:
			queueId = mqRequest.getQueueId();
			content = mqRequest.getContent();
			boolean result = redisTemplate.offer(queueId, content);
			mqResponse.setContent(result);
			break;
			
		case poll:
			queueId = mqRequest.getQueueId();
			content = redisTemplate.poll(queueId);
			mqResponse.setContent(content);
			break;

		default:
			break;
		}
		return mqResponse;
	}

}

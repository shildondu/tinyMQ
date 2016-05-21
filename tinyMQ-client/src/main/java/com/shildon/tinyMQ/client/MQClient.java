package com.shildon.tinyMQ.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shildon.tinyMQ.client.handler.MQClientDecoder;
import com.shildon.tinyMQ.client.handler.MQClientEncoder;
import com.shildon.tinyMQ.core.MQRequest;
import com.shildon.tinyMQ.core.MQResponse;
import com.shildon.tinyMQ.core.MQTransferType;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端。
 * @author shildon<shildondu@gmail.com>
 * @date May 6, 2016
 */
public class MQClient extends SimpleChannelInboundHandler<MQResponse> {
	
	private String host;
	private int port;
	
	private MQResponse mqResponse;
	
	// 这里不使用Condition是因为不需要用到Lock来同步代码块
	private final Object object = new Object();
	
	private final Log log = LogFactory.getLog(MQClient.class);
	
	public MQClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	private void execute(MQRequest mqRequest) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new MQClientEncoder())
							.addLast(new MQClientDecoder(MQResponse.class))
							.addLast(MQClient.this);
					}
				})
				.option(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
			channelFuture.channel().writeAndFlush(mqRequest).sync();
			
			synchronized (object) {
				object.wait();
			}
			
			channelFuture.channel().close().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public boolean offer(String queueId, Object content) throws InterruptedException {
		MQRequest mqRequest = new MQRequest().setMqTransferType(MQTransferType.offer)
				.setQueueId(queueId).setContent(content);
		execute(mqRequest);
		
		if (null != mqResponse) {
			return true;
		} else {
			return false;
		}
	}
	
	public Object poll(String queueId) throws InterruptedException {
		MQRequest mqRequest = new MQRequest().setMqTransferType(MQTransferType.poll)
				.setQueueId(queueId);
		execute(mqRequest);
		
		if (null != mqResponse) {
			return mqResponse.getContent();
		} else {
			return null;
		}
	}
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, MQResponse msg) throws Exception {
		log.debug("enter client handler ...");
		log.debug("mq response type: " + msg.getMqTransferType());
		this.mqResponse = msg;
		
		synchronized (object) {
			object.notifyAll();
		}
	}

}

package com.shildon.tinymq.client;

import com.shildon.tinymq.client.handler.MqClientDecoder;
import com.shildon.tinymq.client.handler.MqClientEncoder;
import com.shildon.tinymq.core.MqRequest;
import com.shildon.tinymq.core.MqResponse;
import com.shildon.tinymq.core.MqTransferType;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端。
 *
 * @author shildon<shildondu @ gmail.com>
 * @date May 6, 2016
 */
public class MqClient extends SimpleChannelInboundHandler<MqResponse> {

	private final String host;
	private final int port;

	private MqResponse mqResponse;

	// 这里不使用Condition是因为不需要用到Lock来同步代码块
	private final Object object = new Object();

	private static final Logger LOGGER = LoggerFactory.getLogger(MqClient.class);

	public MqClient(final String host, final int port) {
		this.host = host;
		this.port = port;
	}

	private void execute(final MqRequest mqRequest) throws InterruptedException {
		final EventLoopGroup group = new NioEventLoopGroup();

		try {
			final Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(final SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new MqClientEncoder())
									.addLast(new MqClientDecoder(MqResponse.class))
									.addLast(MqClient.this);
						}
					})
					.option(ChannelOption.SO_KEEPALIVE, true);

			final ChannelFuture channelFuture = bootstrap.connect(this.host, this.port).sync();
			channelFuture.channel().writeAndFlush(mqRequest).sync();

			synchronized (this.object) {
				this.object.wait();
			}

			channelFuture.channel().close().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	public boolean offer(final String queueId, final Object content) throws InterruptedException {
		final MqRequest mqRequest = new MqRequest().setMqTransferType(MqTransferType.offer)
				.setQueueId(queueId).setContent(content);
		this.execute(mqRequest);

		return null != this.mqResponse;
	}

	public Object poll(final String queueId) throws InterruptedException {
		final MqRequest mqRequest = new MqRequest().setMqTransferType(MqTransferType.poll)
				.setQueueId(queueId);
		this.execute(mqRequest);

		if (null != this.mqResponse) {
			return this.mqResponse.getContent();
		} else {
			return null;
		}
	}

	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, final MqResponse msg) throws Exception {
		LOGGER.debug("enter client handler ...");
		LOGGER.debug("mq response type: " + msg.getMqTransferType());
		this.mqResponse = msg;

		synchronized (this.object) {
			this.object.notifyAll();
		}
	}

}

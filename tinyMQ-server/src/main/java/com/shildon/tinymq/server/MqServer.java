package com.shildon.tinymq.server;

import com.shildon.tinymq.core.MqRequest;
import com.shildon.tinymq.server.handler.MqServerDecoder;
import com.shildon.tinymq.server.handler.MqServerEncoder;
import com.shildon.tinymq.server.handler.MqServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务器端。
 *
 * @author shildon<shildondu @ gmail.com>
 * @date May 3, 2016
 */
public class MqServer {

	private final int port;

	public MqServer(final int port) {
		this.port = port;
	}

	public void run() throws Exception {
		final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		final EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			final ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(final SocketChannel ch) {
							ch.pipeline()
									.addLast(new MqServerEncoder())
									.addLast(new MqServerDecoder(MqRequest.class))
									.addLast(new MqServerHandler());
						}
					})
					// set connections queue size
					.option(ChannelOption.SO_BACKLOG, 1024)
					// no delay for sending packet
					.childOption(ChannelOption.TCP_NODELAY, true);

			final ChannelFuture channelFuture = serverBootstrap.bind(this.port).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
}

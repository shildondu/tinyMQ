package com.shildon.tinymq.server;

import com.shildon.tinymq.core.MqRequest;
import com.shildon.tinymq.server.codec.MessageDecoder;
import com.shildon.tinymq.server.codec.MessageEncoder;
import com.shildon.tinymq.server.handler.MessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务器端
 *
 * @author shildon
 */
public class MessageServer {

	private final int port;

	public MessageServer(final int port) {
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
									.addLast(new MessageEncoder())
									.addLast(new MessageDecoder())
									.addLast(new MessageHandler());
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

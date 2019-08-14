package com.mzc.nettyTest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {

    public void bind(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChildChannelHandler());
        try {
            //绑定端口，同步等待结果
            ChannelFuture future = bootstrap.bind(port).sync();
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline()
                    /******支持粘包*****/
//                    .addLast(new LineBasedFrameDecoder(1024))       //粘包（以换行符为分割进行粘包）
                    //.addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("$".getBytes())))       //粘包（以$作为结束符）
                    //.addLast(new StringDecoder())               //将上一步粘好的整包转换为字符串形式
                    /******支持粘包*****/
                    //第一个参数：包最大值   第二个：长度域的偏移量   第三个：长度域长度
                    .addLast("frameDecoder", new TestDecoderHandler(65535, 0, 2, 0, 2))
                    .addLast(new StringDecoder())
                    .addLast(new TimerServerHandler());         //拿到上面操作转换后的数据（如果没有添加数据格式转换则接收到的是ByteBuf数据）
        }
    }

    public static void main(String[] args) {
        int port = 8999;
        new TimeServer().bind(port);
    }
}

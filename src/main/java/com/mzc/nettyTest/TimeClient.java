package com.mzc.nettyTest;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.sql.Timestamp;

public class TimeClient {

    public void connect(int port, String host) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //客户端如果要支持粘包，逻辑和服务器一样，都是先添加一个包的分隔逻辑，直到解析器找到了数据包的结束符才一次性将粘好的整包发送给后续处理器
                        socketChannel.pipeline()
//                                .addLast("frameDecoder", new TestDecoderHandler(65535, 0, 2, 0, 2))
                                .addLast(new TimerClientHandler());
                    }
                });
        try {
            ChannelFuture future = b.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
//        int port = 8999;
//        String host = "127.0.0.1";
//        new TimeClient().connect(port, host);
    }

}


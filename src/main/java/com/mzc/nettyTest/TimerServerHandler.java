package com.mzc.nettyTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

public class TimerServerHandler extends ChannelHandlerAdapter {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        System.out.println("handlerAdded");
        String currentTime = new Date(System.currentTimeMillis()).toString();

        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        //为了防止频繁唤醒Selector进行发送消息，Netty的write方法并不直接将消息写入SocketChannel中，调用write方法只是将待发送的消息放到发送缓存数组中，再通过调用flush方法，将发送缓冲区中的消息全部写道SocketChannel中
        ctx.writeAndFlush(resp);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("channelRead");
        /**如果在使用ServerBootstrap的childHandler中不设置对消息的粘包/解码等操作，此处返回的都会是ByteBuf，需要自己处理粘包及解码等操作*/
        /*ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "utf-8");*/
        /**如果在使用ServerBootstrap的childHandler中不设置对消息的粘包/解码等操作，此处返回的都会是ByteBuf，需要自己处理粘包及解码等操作*/
        String body = (String) msg;         //在设置解码器之后可以将消息转换为特定类型数据，此处是利用换行符进行粘包操作并将整包解析出来发送到此handler做具体处理
        System.out.printf("channel read body : %s\n", body);
        String currentTime = "QUERY_TIME_ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";

        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        //为了防止频繁唤醒Selector进行发送消息，Netty的write方法并不直接将消息写入SocketChannel中，调用write方法只是将待发送的消息放到发送缓存数组中，再通过调用flush方法，将发送缓冲区中的消息全部写道SocketChannel中
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        System.out.println("channelReadComplete!");
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }
}

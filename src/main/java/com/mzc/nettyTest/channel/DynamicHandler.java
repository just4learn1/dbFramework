package com.mzc.nettyTest.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DynamicHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {

    }

    public static void main(String[] args) throws IOException {
        List<String> list = Arrays.asList("asd", "bbb", "qwer");
        MessagePack messagePack = new MessagePack();
        //序列化
        byte[] raw = messagePack.write(list);
        //反序列化
        List<String> l2 = new MessagePack().read(raw, Templates.tList(Templates.TString));
        System.out.println(l2);
    }
}

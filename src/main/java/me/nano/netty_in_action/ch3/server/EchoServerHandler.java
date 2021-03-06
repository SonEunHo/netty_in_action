package me.nano.netty_in_action.ch3.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("[channelRead]");
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        ctx.pipeline().write(in);
        //ctx.write(in)으로 호출하면 현재 기준으로 앞에 있는 outbound handler부터 처리하게 된다.
        //후에 배우겠지만 핸들러들은 ChannelHandlerContext를 통해 linked list형식으로 파이프라인을 구성하게 된다.
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[channelReadComplete]");
        ctx.pipeline().writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("[exception caught]");
        cause.printStackTrace();
        ctx.close();
    }
}

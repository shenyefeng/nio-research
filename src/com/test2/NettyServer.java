package com.test2;

import java.io.UnsupportedEncodingException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // LineBasedFrameDecoder按行分割消息
                            pipeline.addLast(new LineBasedFrameDecoder(80));
                            // 再按UTF-8编码转成字符串
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture f = b.bind(8080).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}

class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 接收到新的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
//        try {
//            // 接收客户端的数据
//            ByteBuf in = (ByteBuf) msg;
//            System.out.println("channelRead:" + in.toString(CharsetUtil.UTF_8));
//            // 发送到客户端
//            byte[] responseByteArray = "你好".getBytes("UTF-8");
//            ByteBuf out = ctx.alloc().buffer(responseByteArray.length);
//            out.writeBytes(responseByteArray);
//            ctx.writeAndFlush(out);
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }
            
         // msg经过StringDecoder后类型不再是ByteBuf而是String
            String line = (String) msg;
            System.out.println("channelRead:" + line);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("channelInactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
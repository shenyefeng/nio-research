package com.test2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaServer {

    public static void main(String[] args) throws IOException {
        IoAcceptor acceptor = new NioSocketAcceptor();
        // 添加一个Filter，用于接收、发送的内容按照"\r\n"分割
        acceptor.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), "\r\n", "\r\n")));
        acceptor.setHandler(new MinaServerHandler());
        acceptor.bind(new InetSocketAddress(8080));
    }

}

class MinaServerHandler extends IoHandlerAdapter {

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    // 接收到新的数据
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

//        // 接收客户端的数据
//        IoBuffer ioBuffer = (IoBuffer) message;
//        byte[] byteArray = new byte[ioBuffer.limit()];
//        ioBuffer.get(byteArray, 0, ioBuffer.limit());
//        System.out.println("messageReceived:" + new String(byteArray, "UTF-8"));
//
//        // 发送到客户端
//        byte[] responseByteArray = "你好".getBytes("UTF-8");
//        IoBuffer responseIoBuffer = IoBuffer.allocate(responseByteArray.length);
//        responseIoBuffer.put(responseByteArray);
//        responseIoBuffer.flip();
//        session.write(responseIoBuffer);
        
     // 接收客户端的数据，这里接收到的不再是IoBuffer类型，而是字符串
        String line = (String) message;
        System.out.println("messageReceived:" + line);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("sessionCreated");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("sessionClosed");
    }
}
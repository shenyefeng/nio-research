package com.test2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpClient {

	public static void main(String[] args) throws IOException, InterruptedException {


		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;

		try{

			socket = new Socket("localhost", 8080);      
			out = socket.getOutputStream();
			in = socket.getInputStream();

			// 请求服务器
            String lines = "床前明月光\r\n疑是地上霜\r\n举头望明月\r\n低头思故乡\r\n";
            byte[] outputBytes = lines.getBytes("UTF-8");
            out.write(outputBytes);
            out.flush();

		} finally {
			// 关闭连接
			in.close();
			out.close();
			socket.close();
		}

	}

}
package com.astocoding.io.bio.serverAndClient;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/8/14 11:39
 */
public class Service {
    @SneakyThrows
    public static void main(String[] args) {
        byte[] buffer = new byte[1024];
        ServerSocket serverSocket = new ServerSocket(9090);
        System.out.println("服务端启动成功");
        while(true){
            System.out.println("等待客户端连接");
            Socket socket = serverSocket.accept();
            System.out.println("客户端连接成功");
            System.out.println("等待客户端发送数据");
            socket.getInputStream().read(buffer);
           System.out.println("服务端已经接收到数据");
            System.out.println();
            String content = new String(buffer);
            System.out.println("接收到的数据 " + content);
        }
    }
}

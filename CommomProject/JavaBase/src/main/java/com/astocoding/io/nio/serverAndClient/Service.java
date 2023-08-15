package com.astocoding.io.nio.serverAndClient;

import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/8/14 14:51
 */
public class Service {



    private static List<SocketChannel> socketChannelList = new ArrayList<>();
    @SneakyThrows
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9090));
        serverSocketChannel.configureBlocking(false);
        while (!Thread.interrupted()){
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel == null){
                System.out.println("等待客户端连接");
                Thread.sleep(5000);
            }else{
                socketChannel.configureBlocking(false);
                socketChannelList.add(socketChannel);
                System.out.println("客户端连接成功");
            }

            for (SocketChannel channel : socketChannelList) {
                int readCount = channel.read(buffer);
                if (readCount != 0){
                    buffer.flip();
                    String content = Charset.forName("utf-8").decode(buffer).toString();
                    System.out.println("接收到的数据 " + content);
                    buffer.clear();
                }
            }

        }
    }
}


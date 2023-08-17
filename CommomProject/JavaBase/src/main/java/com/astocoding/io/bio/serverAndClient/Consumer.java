package com.astocoding.io.bio.serverAndClient;

import lombok.SneakyThrows;

import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/8/14 11:44
 */
public class Consumer {
    @SneakyThrows
    public static void main(String[] args) {
        Socket socket = new Socket("127.0.0.1",9090);
        String message = null;
        Scanner sc = new Scanner(System.in);
        message = sc.nextLine();
        socket.getOutputStream().write(message.getBytes());
        socket.close();
    }
}

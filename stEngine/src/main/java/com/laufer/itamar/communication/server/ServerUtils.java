package com.laufer.itamar.communication.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerUtils {
    public static void send(Socket socket, String msg){
        try {
            new PrintWriter(socket.getOutputStream(), true).println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.laufer.itamar.engine;

import com.laufer.itamar.communication.server.GameServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        //Todo do some tests here
        //Todo handle die of loader with safe boat
        //GameServer.runServer();
        GameServer server = new GameServer();
        server.runServer();
    }
}

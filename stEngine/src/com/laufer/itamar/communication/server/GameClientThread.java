package com.laufer.itamar.communication.server;

import java.net.Socket;


public class GameClientThread implements Runnable {
    private PlayerClient client;

    GameClientThread(GameServer server, Socket clientSocket) {
        this.client = new PlayerClient(clientSocket, server);
    }

    public void run() {
        client.run();
    }
}

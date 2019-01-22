package com.laufer.itamar.communication.server;

import java.net.Socket;


public class GameClientThread implements Runnable {
    private PlayerClient client;
    private final GameServer server;

    public GameClientThread(GameServer server, Socket clientSocket) {
        this.client = new PlayerClient(clientSocket, server);
        this.server = server;
    }

    public void run() {
        client.run();
    }
}

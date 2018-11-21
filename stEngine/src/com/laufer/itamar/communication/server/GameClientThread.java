package com.laufer.itamar.communication.server;


import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class GameClientThread implements Runnable {
    private PlayerClient client;
    private final GameServer server;

    public GameClientThread(GameServer server, Socket clientSocket) {
        this.client = new PlayerClient(clientSocket);
        this.server = server;
    }

    public void run() {
        System.out.println("new client has connected " + client.getSocket());
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
            String inputLine;
            String name;
            while ((inputLine = in.readLine()) != null) {
                if(inputLine.contains("join")){
                    if(client.getGame() == null) {
                        name = inputLine.replace("join ", "");
                        client.setName(name);
                        server.joinAGame(client);
                    }
                    else
                        ServerUtils.send(client.getSocket(), "You are already in a game");
                }
                else {
                    if (client.getGame() == null)
                        ServerUtils.send(client.getSocket(), "You didn't join a game yet");
                    else
                        ServerUtils.send(client.getSocket(), "Opponent is " + client.getGame().getOtherPlayer(client).getName());
                    }
                }
        } catch (SocketException e) { //client has disconnected
            System.out.println("client has disconnected " + client.getSocket());
            server.disconnect(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

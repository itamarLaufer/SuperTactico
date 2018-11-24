package com.laufer.itamar.communication.server;

import com.laufer.itamar.engine.SuperTacticoGame;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    private final int PORT = 1978;
    private PlayerClient waiting = null;
    private List<GameClient> games;
    private ExecutorService executor;

    public GameServer() {
        games = new LinkedList<>();
        executor = Executors.newFixedThreadPool(8);
    }

    public void runServer() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();

        }
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client
            executor.execute(new GameClientThread(this, socket));
        }
    }

    synchronized void joinAGame(PlayerClient client) {
        if (waiting != null) {
            GameClient game = new GameClient(new SuperTacticoGame(waiting.getName(), client.getName(), waiting.getGame().getGame(), client.getGame().getGame()), waiting, client);
            games.add(game);
            ServerUtils.send(waiting.getSocket(), "game found!");
            ServerUtils.send(client.getSocket(), "game found!");
            waiting.setGame(game);
            client.setGame(game);
            waiting = null;
        }
        else {
            waiting = client;
            executor.execute(new TaskForTime(20000) {
                @Override
                public boolean isDone() {
                    return waiting == null;
                }

                @Override
                public void handleNotDone() {
                    ServerUtils.send(waiting.getSocket(), "couldn't find :(");
                    waiting = null;
                }
            });
        }
    }

    public void disconnect(PlayerClient disconnecting){
        if(disconnecting.getGame() != null) { //in a middle of a game -> needs to update opponent
            ServerUtils.send(disconnecting.getGame().getOtherPlayer(disconnecting).getSocket(), "Your opponent has disconnected");
            disconnecting.getGame().getOtherPlayer(disconnecting).setGame(null);
            games.remove(disconnecting.getGame());
        }
        else{ // not in a middle of a game, but maybe while waiting for an opponent
            synchronized (this) {
                if (waiting == disconnecting)
                    waiting = null;
            }
        }
        try {
            disconnecting.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

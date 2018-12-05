package com.laufer.itamar.communication.server;

import com.laufer.itamar.engine.SuperTacticoGame;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;

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
            SuperTacticoGame game = new SuperTacticoGame(waiting.getName(), client.getName(), waiting.getGame().getGame(), client.getGame().getGame());
            GameClient gameClient = new GameClient(game, waiting, client);
            games.add(gameClient);
            waiting.setGame(gameClient);
            client.setGame(gameClient);
            JSONObject update = new JSONObject();
            update.put("pieces", JsonUtils.listToJsonArray(game.getOtherPlayer().getLivingPieces()));
            update.put("starts", 1);
            update.put("opponent", client.getName());
            ServerUtils.send(waiting.getSocket(), update.toJSONString());
            game.nextTurn();
            game.turnBoard();
            update.put("pieces", JsonUtils.listToJsonArray(game.getOtherPlayer().getLivingPieces()));
            update.put("starts", 0);
            update.put("opponent", waiting.getName());
            ServerUtils.send(client.getSocket(), update.toJSONString());
            game.previousTurn();
            game.turnBoard();
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
        if(!disconnecting.getGame().getGame().isFake()) { //in a middle of a game -> needs to update opponent
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

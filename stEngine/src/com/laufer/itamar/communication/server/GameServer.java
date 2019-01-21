package com.laufer.itamar.communication.server;

import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.SuperTacticoGame;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
            waiting.setPlayer(game.getPlayers()[0]);
            client.setGame(gameClient);
            client.setPlayer(game.getPlayers()[1]);
            JSONObject update = new JSONObject();
            List<Piece>pieces = new LinkedList<>(game.getOtherPlayer().getLivingPieces());
            Collections.shuffle(pieces);
            update.put("pieces", JsonUtils.listToJsonArray(pieces, new String[]{"0"}));
            update.put("newIds", game.getCurrentPlayer().getLivingPieces().stream().map(Piece::getId).collect(Collectors.toList()));
            update.put("turn", 1);
            update.put("opponent", client.getName());
            ServerUtils.send(waiting.getSocket(), "4_" + update.toJSONString());
            game.turnBoard();
            pieces = new LinkedList<>(game.getCurrentPlayer().getLivingPieces());
            Collections.shuffle(pieces);
            update.put("pieces", JsonUtils.listToJsonArray(pieces, new String[]{"1"}));
            update.put("newIds", game.getOtherPlayer().getLivingPieces().stream().map(Piece::getId).collect(Collectors.toList()));
            update.put("turn", 0);
            update.put("opponent", waiting.getName());
            ServerUtils.send(client.getSocket(), "4_" + update.toJSONString());
            GameClientThread.timeTheTurn(client, this);

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
            ServerUtils.send(disconnecting.getGame().getOtherPlayer(disconnecting).getSocket(), "0_Opponent has disconnected");
            disconnecting.getGame().getOtherPlayer(disconnecting).setGame(null);
            removeGame(disconnecting.getGame());
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

    public ExecutorService getExecutor() {
        return executor;
    }
    public void removeGame(GameClient gameClient){
        games.remove(gameClient);
    }
}

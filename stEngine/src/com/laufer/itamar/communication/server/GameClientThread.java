package com.laufer.itamar.communication.server;


import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.SuperTacticoGame;
import com.laufer.itamar.engine.orders.MoveOrder;
import com.laufer.itamar.engine.orders.Order;
import com.laufer.itamar.org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class GameClientThread implements Runnable {
    private PlayerClient client;
    private final GameServer server;

    public GameClientThread(GameServer server, Socket clientSocket) {
        this.client = new PlayerClient(clientSocket);
        this.server = server;
    }

    public void run() {
        SuperTacticoGame game = new SuperTacticoGame();
        System.out.println("new client has connected " + client.getSocket());
        client.setGame(new GameClient(game, null, null));
        JSONObject update = new JSONObject();
        update.put("board", game.getBoardAsJson());
        update.put("pieces", JsonUtils.listToJsonArray(game.getAllLivingPieces(), new String[]{String.valueOf(game.getCurrentPlayer().getId())}));
        ServerUtils.send(client.getSocket(), "4_" + update.toJSONString());
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
            String inputLine;
            String name;
            while ((inputLine = in.readLine()) != null) {
                String[]args = inputLine.split("_");
                if(args.length == 0) {
                    ServerUtils.send(client.getSocket(), "0_" + "invalid request");
                    continue;
                }
                switch (args[0]) {
                    case "1":
                        handleJoinRequest(args);
                        break;
                    case "3":
                        handlePossibleOrdersRequest(args);
                        break;
                    case "2":
                        handleExecuteOrderRequest(args);
                        break;
                    default:
                        ServerUtils.send(client.getSocket(), "0_" + "wrong request");
                        break;
                }
                }
        } catch (SocketException e) { //client has disconnected
            System.out.println("client has disconnected " + client.getSocket());
            server.disconnect(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleExecuteOrderRequest(String[] args) {
        SuperTacticoGame game = client.getGame().getGame();
        JSONObject update;
        if (args.length == 3) {
            int actorId;
            int orderId;
            try {
                actorId = Integer.parseInt(args[1]);
                orderId = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                ServerUtils.send(client.getSocket(), "0_" + "invalid request");
                return;
            }
            Piece actor = client.getGame().getGame().getPieceById(actorId);
            if (actor != null) {
                if (!client.getGame().getGame().isFake()) {
                    //Todo validate, Execute the order, update all players, move turn to the next player
                    if(client.getPlayer() != client.getGame().getGame().getCurrentPlayer()){
                        ServerUtils.send(client.getSocket(), "0_" + "not your turn");
                        return;
                    }
                    List<Order> orders = actor.getPossibleOrders();
                    if (orderId < orders.size() && orderId >= 0) {
                        Order order = orders.get(orderId);
                        order.execute();
                        update = order.orderDelta(client.getGame().getGame().getCurrentPlayer());
                        update.put("turn", 0);
                        //Todo maybe another data should be added like battle results, safe boat etc.
                        ServerUtils.send(client.getSocket(), "5_" + update.toJSONString());
                        client.getGame().getGame().turnBoard();
                        update = order.orderDelta(client.getGame().getGame().getOtherPlayer());
                        update.put("turn", 1);
                        //Todo maybe another data should be added like battle results, safe boat etc.
                        ServerUtils.send(client.getGame().getOtherPlayer(client).getSocket(), "4_" + update.toJSONString());

                        client.getGame().getGame().nextTurn();
                        timeTheTurn(client, server);
                    } else
                        ServerUtils.send(client.getSocket(), "0_" + "invalid request");
                } else {
                    List<MoveOrder> orders = actor.getLocateOrders();
                    if (orderId < orders.size() && orderId >= 0) {
                        Order order = orders.get(orderId);
                        order.execute();
                        update = order.orderDelta(client.getGame().getGame().getCurrentPlayer());
                        ServerUtils.send(client.getSocket(), "4_" + update.toJSONString());
                    } else
                        ServerUtils.send(client.getSocket(), "0_" + "invalid request");

                }
            } else
                ServerUtils.send(client.getSocket(), "0_" + "invalid id");
        } else
            ServerUtils.send(client.getSocket(), "0_" + "invalid request");
    }

    public static void timeTheTurn(PlayerClient client, GameServer server) {
        SuperTacticoGame finalGame = client.getGame().getGame();
        server.getExecutor().execute(new TurnTaskForTime(1200000, finalGame.getTurns()) { //Todo constant
            @Override
            public boolean isDone() {
                return getTurn() < finalGame.getTurns();
            }

            @Override
            public void handleNotDone() {
                try {
                    client.getGame().getOtherPlayer(client).getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                client.setGame(new GameClient(new SuperTacticoGame(), null, null));
                server.removeGame(client.getGame());
            }
        });
    }

    private void handlePossibleOrdersRequest(String[] args) {
        JSONObject update;
        if (args.length == 2) {
            int actorId;
            try {
                actorId = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                ServerUtils.send(client.getSocket(), "0_" + "invalid request");
                return;
            }
            if(client.getPlayer() != client.getGame().getGame().getCurrentPlayer()){
                ServerUtils.send(client.getSocket(), "0_" + "not your turn"); //Todo support possible orders request not at your turn
                return;
            }
            Piece actor = client.getGame().getGame().getPieceById(actorId);
            if (actor != null) {
                update = new JSONObject();
                update.put("actorId", actor.getId());
                if (!client.getGame().getGame().isFake()) {
                    update.put("orders", JsonUtils.listToJsonArray(actor.getPossibleOrders(), null));
                } else {
                    update.put("orders", JsonUtils.listToJsonArray(actor.getLocateOrders(), null));
                }
                ServerUtils.send(client.getSocket(), "5_" + update.toJSONString());
            } else
                ServerUtils.send(client.getSocket(), "0_" + "invalid id");
        } else
            ServerUtils.send(client.getSocket(), "0_" + "invalid request");
    }

    private void handleJoinRequest(String[] args) {
        String name;
        if (client.getGame().getGame().isFake()) {
            if (args.length == 2) {
                name = args[1];
                client.setName(name);
                server.joinAGame(client);
            } else
                ServerUtils.send(client.getSocket(), "0_" + "invalid request");
        } else
            ServerUtils.send(client.getSocket(), "You are already in a game");
    }
}

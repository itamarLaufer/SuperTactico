package com.laufer.itamar.communication.server;


import com.laufer.itamar.engine.Main;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.SuperTacticoGame;
import com.laufer.itamar.engine.orders.MoveOrder;
import com.laufer.itamar.engine.orders.Order;
import org.json.simple.JSONObject;

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
                        if (client.getGame().getGame().isFake()) {
                            if (args.length == 2) {
                                name = args[1];
                                client.setName(name);
                                server.joinAGame(client);
                            } else
                                ServerUtils.send(client.getSocket(), "0_" + "invalid request");
                        } else
                            ServerUtils.send(client.getSocket(), "You are already in a game");
                        break;
                    case "3":
                        if (args.length == 2) {
                            int actorId;
                            try {
                                actorId = Integer.parseInt(args[1]);
                            } catch (NumberFormatException e) {
                                ServerUtils.send(client.getSocket(), "0_" + "invalid request");
                                continue;
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
                        break;
                    case "2":
                        if (args.length == 3) {
                            int actorId;
                            int orderId;
                            try {
                                actorId = Integer.parseInt(args[1]);
                                orderId = Integer.parseInt(args[2]);
                            } catch (NumberFormatException e) {
                                ServerUtils.send(client.getSocket(), "0_" + "invalid request");
                                continue;
                            }
                            Piece actor = client.getGame().getGame().getPieceById(actorId);
                            if (actor != null) {
                                if (!client.getGame().getGame().isFake()) {
                                    //Todo validate, Execute the order, update all players, move turn to the next player
                                    if(client.getPlayer() != client.getGame().getGame().getCurrentPlayer()){
                                        ServerUtils.send(client.getSocket(), "0_" + "not your turn");
                                        continue;
                                    }
                                    List<Order> orders = actor.getPossibleOrders();
                                    if (orderId < orders.size() && orderId >= 0) {
                                        Piece[] change = orders.get(orderId).execute();
                                        update = new JSONObject();
                                        update.put("pieces", JsonUtils.arrayToJsonArray(change, new String[]{String.valueOf(game.getCurrentPlayer().getId())}));
                                        update.put("turn", client.getGame().getGame().getCurrentPlayer().getId() ^ 1);
                                        //Todo maybe another data should be added like battle results, safe boat etc.
                                        ServerUtils.send(client.getSocket(), "5_" + update.toJSONString());
                                        client.getGame().getGame().turnBoard();

                                        update.put("pieces", JsonUtils.arrayToJsonArray(change, new String[]{String.valueOf(client.getGame().getGame().getCurrentPlayer().getId() ^ 1)}));
                                        //Todo maybe another data should be added like battle results, safe boat etc.
                                        ServerUtils.send(client.getGame().getOtherPlayer(client).getSocket(), "5_" + update.toJSONString());

                                        client.getGame().getGame().nextTurn();
                                        SuperTacticoGame finalgame = client.getGame().getGame();
                                        this.server.getExecutor().execute(new TurnTaskForTime(5000, finalgame.getTurns()) {
                                            @Override
                                            public boolean isDone() {
                                                System.out.println("isDone");
                                                return getTurn() < finalgame.getTurns();
                                            }

                                            @Override
                                            public void handleNotDone() {
                                                try {
                                                    client.getGame().getOtherPlayer(client).getSocket().close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                ServerUtils.send(client.getSocket(), "0_opponent has disconnected");
                                                client.setGame(new GameClient(new SuperTacticoGame(), null, null));
                                                server.removeGame(client.getGame());
                                            }
                                        });
                                    } else
                                        ServerUtils.send(client.getSocket(), "0_" + "invalid request");
                                } else {
                                    System.out.println("banana");
                                    List<MoveOrder> orders = actor.getLocateOrders();
                                    if (orderId < orders.size() && orderId >= 0) {
                                        Piece[] change = orders.get(orderId).execute();
                                        update = new JSONObject();
                                        update.put("pieces", JsonUtils.arrayToJsonArray(change, new String[]{String.valueOf(game.getCurrentPlayer().getId())}));
                                        ServerUtils.send(client.getSocket(), "5_" + update.toJSONString());
                                    } else
                                        ServerUtils.send(client.getSocket(), "0_" + "invalid request");

                                }
                            } else
                                ServerUtils.send(client.getSocket(), "0_" + "invalid id");
                        } else
                            ServerUtils.send(client.getSocket(), "0_" + "invalid request");
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
}

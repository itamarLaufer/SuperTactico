package com.laufer.itamar.communication.server;


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
        update.put("pieces", JsonUtils.listToJsonArray(game.getAllLivingPieces()));
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
                        if (game.isFake()) {
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
                            Piece actor = game.getPieceById(actorId);
                            if (actor != null) {
                                if (!game.isFake()) {
                                    ServerUtils.send(client.getSocket(), "5_" + JsonUtils.listToJsonArray(actor.getPossibleOrders()));
                                } else {
                                    ServerUtils.send(client.getSocket(), "5_" + JsonUtils.listToJsonArray(actor.getLocateOrders()));
                                }
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
                            Piece actor = game.getPieceById(actorId);
                            if (actor != null) {
                                if (!game.isFake()) {
                                    //Todo validate, Execute the order, update all players, move turn to the next player
                                } else {
                                    List<MoveOrder> orders = actor.getLocateOrders();
                                    if (orderId < orders.size() && orderId >= 0) {
                                        System.out.println("banana");
                                        Piece[] change = orders.get(actorId).execute();
                                        update = new JSONObject();
                                        update.put("pieces", JsonUtils.arrayToJsonArray(change));
                                        //Todo maybe another data should be added like battle results, safe boat etc.
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

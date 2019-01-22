package com.laufer.itamar.communication.server;

import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.Player;
import com.laufer.itamar.engine.SuperTacticoGame;
import com.laufer.itamar.engine.orders.MoveOrder;
import com.laufer.itamar.engine.orders.Order;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class PlayerClient {
    private Player player;
    private Socket socket;
    private String name;
    private GameClient game;
    private GameServer server;

    public PlayerClient(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
        this.name = ""; //name will be initialized later with setName()
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameClient getGame() {
        return game;
    }

    public void setGame(GameClient game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public void run() {
        SuperTacticoGame game = new SuperTacticoGame();
        System.out.println("new client has connected " + this.getSocket());
        this.setGame(new GameClient(game, null, null));
        JSONObject update = new JSONObject();
        update.put("board", game.getBoardAsJson());
        update.put("pieces", JsonUtils.listToJsonArray(game.getAllLivingPieces(), new String[]{String.valueOf(game.getCurrentPlayer().getId())}));
        ServerUtils.send(this.getSocket(), "4_" + update.toJSONString());
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.getSocket().getInputStream()));
            String inputLine;
            String name;
            while ((inputLine = in.readLine()) != null) {
                String[]args = inputLine.split("_");
                if(args.length == 0) {
                    ServerUtils.send(this.getSocket(), "0_" + "invalid request");
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
                        ServerUtils.send(this.getSocket(), "0_" + "wrong request");
                        break;
                }
            }
        } catch (SocketException e) { //client has disconnected
            System.out.println("client has disconnected " + this.getSocket());
            server.disconnect(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleExecuteOrderRequest(String[] args) {
        SuperTacticoGame game = this.getGame().getGame();
        JSONObject update;
        if (args.length == 3) {
            int actorId;
            int orderId;
            try {
                actorId = Integer.parseInt(args[1]);
                orderId = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                ServerUtils.send(this.getSocket(), "0_" + "invalid request");
                return;
            }
            Piece actor = this.getGame().getGame().getPieceById(actorId);
            if (actor != null) {
                if (!this.getGame().getGame().isFake()) {
                    //Todo validate, Execute the order, update all players, move turn to the next player
                    if(this.getPlayer() != this.getGame().getGame().getCurrentPlayer()){
                        ServerUtils.send(this.getSocket(), "0_" + "not your turn");
                        return;
                    }
                    List<Order> orders = actor.getPossibleOrders();
                    if (orderId < orders.size() && orderId >= 0) {
                        Order order = orders.get(orderId);
                        order.execute();
                        update = order.orderDelta(this.getGame().getGame().getCurrentPlayer());
                        update.put("turn", 0);
                        //Todo maybe another data should be added like battle results, safe boat etc.
                        ServerUtils.send(this.getSocket(), "5_" + update.toJSONString());
                        this.getGame().getGame().turnBoard();
                        update = order.orderDelta(this.getGame().getGame().getOtherPlayer());
                        update.put("turn", 1);
                        //Todo maybe another data should be added like battle results, safe boat etc.
                        ServerUtils.send(this.getGame().getOtherPlayer(this).getSocket(), "4_" + update.toJSONString());

                        this.getGame().getGame().nextTurn();
                        timeTheTurn(server);
                    } else
                        ServerUtils.send(this.getSocket(), "0_" + "invalid request");
                } else {
                    List<MoveOrder> orders = actor.getLocateOrders();
                    if (orderId < orders.size() && orderId >= 0) {
                        Order order = orders.get(orderId);
                        order.execute();
                        update = order.orderDelta(this.getGame().getGame().getCurrentPlayer());
                        ServerUtils.send(this.getSocket(), "4_" + update.toJSONString());
                    } else
                        ServerUtils.send(this.getSocket(), "0_" + "invalid request");

                }
            } else
                ServerUtils.send(this.getSocket(), "0_" + "invalid id");
        } else
            ServerUtils.send(this.getSocket(), "0_" + "invalid request");
    }

    public void timeTheTurn(GameServer server) {
        PlayerClient current = this;
        SuperTacticoGame finalGame = getGame().getGame();
        server.getExecutor().execute(new TurnTaskForTime(1200000, finalGame.getTurns()) { //Todo constant
            @Override
            public boolean isDone() {
                return getTurn() < finalGame.getTurns();
            }

            @Override
            public void handleNotDone() {
                try {
                    getGame().getOtherPlayer(current).getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setGame(new GameClient(new SuperTacticoGame(), null, null));
                server.removeGame(getGame());
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
                ServerUtils.send(this.getSocket(), "0_" + "invalid request");
                return;
            }
            if(this.getPlayer() != this.getGame().getGame().getCurrentPlayer()){
                ServerUtils.send(this.getSocket(), "0_" + "not your turn"); //Todo support possible orders request not at your turn
                return;
            }
            Piece actor = this.getGame().getGame().getPieceById(actorId);
            if (actor != null) {
                update = new JSONObject();
                update.put("actorId", actor.getId());
                if (!this.getGame().getGame().isFake()) {
                    update.put("orders", JsonUtils.listToJsonArray(actor.getPossibleOrders(), null));
                } else {
                    update.put("orders", JsonUtils.listToJsonArray(actor.getLocateOrders(), null));
                }
                ServerUtils.send(this.getSocket(), "5_" + update.toJSONString());
            } else
                ServerUtils.send(this.getSocket(), "0_" + "invalid id");
        } else
            ServerUtils.send(this.getSocket(), "0_" + "invalid request");
    }

    private void handleJoinRequest(String[] args) {
        String name;
        if (this.getGame().getGame().isFake()) {
            if (args.length == 2) {
                name = args[1];
                this.setName(name);
                server.joinAGame(this);
            } else
                ServerUtils.send(this.getSocket(), "0_" + "invalid request");
        } else
            ServerUtils.send(this.getSocket(), "You are already in a game");
    }
    
}

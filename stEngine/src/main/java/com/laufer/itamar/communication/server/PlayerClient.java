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

public class PlayerClient implements Runnable {
    private static final int TIME_FOR_TURN = 12000000; // Todo change to reasonable number
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

    Socket getSocket() {
        return socket;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
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
        sendPrimaryData();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.getSocket().getInputStream()));
            String inputLine;
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
                    case "8":
                        handleChatMessageRequest(inputLine);
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

    private void handleChatMessageRequest(String message) {
        if(game.getGame().isFake())
            ServerUtils.send(socket, "0_no opponent yet");
        else
            ServerUtils.send(game.getOtherPlayer(this).getSocket(), message);
    }

    private void sendPrimaryData() {
        SuperTacticoGame game = new SuperTacticoGame();
        System.out.println("new client has connected " + this.getSocket());
        this.setGame(new GameClient(game, null, null));
        JSONObject update = new JSONObject();
        update.put("board", game.getBoardAsJson());
        update.put("pieces", JsonUtils.piecesListToVisibileJsonArray(game.getAllLivingPieces()));
        update.put("timeForTurn", TIME_FOR_TURN);
        ServerUtils.send(this.getSocket(), "4_" + update.toJSONString());
    }

    private void handleExecuteOrderRequest(String[] args) {
        if (args.length != 3) {
            ServerUtils.send(this.getSocket(), "0_" + "invalid request");
        }
        else{
            int actorId;
            int orderId;
            try {
                actorId = Integer.parseInt(args[1]);
                orderId = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                ServerUtils.send(this.getSocket(), "0_" + "invalid request no integers");
                return;
            }
            Piece actor = this.getGame().getGame().getPieceById(actorId);
            if (actor == null) {
                ServerUtils.send(this.getSocket(), "0_" + "invalid id");
            }
            else{
                if (!game.getGame().isFake())
                    executeOrderRealGame(orderId, actor);
                else { //is fake
                    executeOrderFakeGame(orderId, actor);
                }
            }
        }
    }

    private void executeOrderFakeGame(int orderId, Piece actor) {
        JSONObject update;List<MoveOrder> orders = actor.getLocateOrders();
        if (orderId >= orders.size() || orderId < 0) {
            ServerUtils.send(this.getSocket(), "0_" + "invalid order id");
        }
        else{
            Order order = orders.get(orderId);
            order.execute();
            update = order.orderDelta(this.getGame().getGame().getCurrentPlayer());
            ServerUtils.send(this.getSocket(), "4_" + update.toJSONString());
        }
    }

    private void executeOrderRealGame(int orderId, Piece actor) {
        if(this.getPlayer() != this.getGame().getGame().getCurrentPlayer())
            ServerUtils.send(this.getSocket(), "0_" + "not your turn");
        else{
            List<Order> orders = actor.getPossibleOrders();
            if (orderId >= orders.size() || orderId < 0)
                ServerUtils.send(this.getSocket(), "0_" + "invalid order id");
            else
                realGameOrderExecution(orderId, orders);
        }
    }

    private void realGameOrderExecution(int orderId, List<Order> orders) {
        JSONObject update;
        Order order = orders.get(orderId);
        order.execute();
        update = order.orderDelta(this.getGame().getGame().getCurrentPlayer());
        update.put("turn", 0);
        //Todo maybe another data should be added like battle results, safe boat etc.
        ServerUtils.send(this.getSocket(), "6_" + update.toJSONString());
        this.getGame().getGame().turnBoard();
        update = order.orderDelta(this.getGame().getGame().getOtherPlayer());
        update.put("turn", 1);
        //Todo maybe another data should be added like battle results, safe boat etc.
        ServerUtils.send(this.getGame().getOtherPlayer(this).getSocket(), "6_" + update.toJSONString());

        this.getGame().getGame().nextTurn();
        timeTheTurn(server);
    }

    void timeTheTurn(GameServer server) {
        PlayerClient current = this;
        SuperTacticoGame finalGame = getGame().getGame();
        server.getExecutor().execute(new TurnTaskForTime(TIME_FOR_TURN, finalGame.getTurns()) { //Todo constant
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
        //validation
        if (args.length == 2) {
            int actorId;
            try {
                actorId = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                ServerUtils.send(this.getSocket(), "0_" + "invalid request no integers");
                return;
            }
            if(!this.game.getGame().isFake() && this.getPlayer() != this.getGame().getGame().getCurrentPlayer()){
                ServerUtils.send(this.getSocket(), "0_" + "not your turn");
                return;
            }
            Piece actor = this.getGame().getGame().getPieceById(actorId);
            if (actor != null) {
                sendPossibleOrdersRespond(actor);
            } else
                ServerUtils.send(this.getSocket(), "0_" + "invalid id");
        }
        else
            ServerUtils.send(this.getSocket(), "0_" + "invalid request");
    }

    private void sendPossibleOrdersRespond(Piece actor) {
        JSONObject update;
        update = new JSONObject();
        update.put("actorId", actor.getId());
        if (!this.getGame().getGame().isFake()) {
            update.put("orders", JsonUtils.listToJsonArray(actor.getPossibleOrders()));
        } else {
            update.put("orders", JsonUtils.listToJsonArray(actor.getLocateOrders()));
        }
        ServerUtils.send(this.getSocket(), "5_" + update.toJSONString());
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

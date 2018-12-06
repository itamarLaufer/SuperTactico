package com.laufer.itamar.communication.server;


import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.SuperTacticoGame;
import org.json.simple.JSONObject;

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
        SuperTacticoGame game = new SuperTacticoGame();
        System.out.println("new client has connected " + client.getSocket());
        client.setGame(new GameClient(game, null, null));
        JSONObject update = new JSONObject();
        update.put("board", game.getBoardAsJson());
        update.put("pieces", JsonUtils.listToJsonArray(game.getAllLivingPieces()));
        ServerUtils.send(client.getSocket(), 4 + "_" + update.toJSONString());
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
            String inputLine;
            String name;
            while ((inputLine = in.readLine()) != null) {
                if(inputLine.startsWith("1_")){
                    if(game.isFake()) {
                        name = inputLine.replace("1_", "");
                        client.setName(name);
                        server.joinAGame(client);
                    }
                    else
                        ServerUtils.send(client.getSocket(), "You are already in a game");
                }
                else if(inputLine.startsWith("3_")){
                    Piece actor = game.getPieceById(Integer.parseInt(inputLine.replace("3_", "")));
                    if(actor == null){
                        ServerUtils.send(client.getSocket(), "0_" + "invalid id");
                        continue;
                    }
                    if(!game.isFake()){
                        ServerUtils.send(client.getSocket(), "5_" + JsonUtils.listToJsonArray(actor.getPossibleOrders()));
                    }
                    else{
                        ServerUtils.send(client.getSocket(), "5_" + JsonUtils.listToJsonArray(actor.getLocateOrders()));
                    }


                }
                else {
                    if (client.getGame().getGame().isFake())
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

package com.laufer.itamar.communication.server;

import com.laufer.itamar.engine.SuperTacticoGame;

public class GameClient {
    private PlayerClient[]clients;
    private SuperTacticoGame game;

    public GameClient(SuperTacticoGame game, PlayerClient client1, PlayerClient client2) {
        this.game = game;
        this.clients = new PlayerClient[]{client1, client2};
    }

    public PlayerClient[] getClients() {
        return clients;
    }

    public SuperTacticoGame getGame() {
        return game;
    }
    public PlayerClient getOtherPlayer(PlayerClient client){
        if(clients[0] == client)
            return clients[1];
        if(clients[1] == client)
            return clients[0];
        return null;
    }
}

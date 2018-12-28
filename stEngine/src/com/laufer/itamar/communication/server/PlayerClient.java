package com.laufer.itamar.communication.server;

import com.laufer.itamar.engine.Player;

import java.net.Socket;

public class PlayerClient {
    private Player player;
    private Socket socket;
    private String name;
    private GameClient game;

    public PlayerClient(Socket socket) {
        this.socket = socket;
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
}

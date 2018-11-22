package com.laufer.itamar.engine;

import com.laufer.itamar.communication.server.GameServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        //Todo do some tests here
        //Todo handle die of loader with safe boat
        GameServer server = new GameServer();
        server.runServer();
    }
    public static void printBoard(SuperTacticoGame game){
        for(int i=0;i<20;i++){
            for(int j=0;j<20;j++){
                System.out.print(game.getLocTypeInLocation(new Location(i, j)));
            }
            System.out.println();
        }
    }
}

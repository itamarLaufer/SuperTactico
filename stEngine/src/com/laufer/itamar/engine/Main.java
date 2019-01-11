package com.laufer.itamar.engine;

import com.laufer.itamar.communication.server.GameServer;



public class Main {

    public static void main(String[] args) {
        //Todo countdown at the first turn
        //Todo make the countdown work
        //Todo send battle results
        //Todo handle die of loader with safe boat
        GameServer server = new GameServer();
        server.runServer();
    }
    public static<T> void printArr(T[][]arr){
        for(int i=0;i<arr.length;i++){
            for(int j=0;j<arr[i].length;j++){
                System.out.print(arr[i][j].toString() + ',');
            }
            System.out.println();
        }
    }
}

package com.laufer.itamar;

import com.laufer.itamar.communication.server.GameServer;


public class App 
{
    public static void main(String[] args) {
        // Todo handle die of loader with safe boat
        //Todo maybe support pause request
        Config.config();
        GameServer server = new GameServer();
        server.runServer();
    }
    public static<T> void printArr(T[][]arr){
        for (T[] anArr : arr) {
            for (T anAnArr : anArr) {
                System.out.print(anAnArr.toString() + ',');
            }
            System.out.println();
        }
    }
}

package com.laufer.itamar.communication.server;

public abstract class TurnTaskForTime extends TaskForTime {
    private int turn;
    public TurnTaskForTime(int time, int turn) {
        super(time);
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }
}

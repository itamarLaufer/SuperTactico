package com.laufer.itamar.engine.Pieces;

public enum SoldierLevel {
    GENERAL(8);
    private int level;

    SoldierLevel(int level){
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

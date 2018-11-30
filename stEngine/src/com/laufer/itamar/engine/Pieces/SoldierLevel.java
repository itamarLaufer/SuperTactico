package com.laufer.itamar.engine.Pieces;

public enum SoldierLevel {
    CORPORAL(0),
    SERGEANT(1),
    SERGEANT_MAJOR(2),
    LIEUTENANT(3),
    AXLE(4),
    MAJOR(5),
    LIEUTENANT_COLONEL(6),
    GENERAL(7),
    LIEUTENANT_GENERAL(8);

    private int level;

    SoldierLevel(int level){
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

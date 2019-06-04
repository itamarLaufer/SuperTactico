package com.laufer.itamar.engine;

public enum BattleResult {
    VICTORY, DEFEAT, TIE;

    public BattleResult reverse(){
        switch (this){
            case VICTORY: return DEFEAT;
            case DEFEAT: return VICTORY;
            case TIE: return TIE;
            default: return null;
        }
    }
}

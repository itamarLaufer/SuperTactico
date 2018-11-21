package com.laufer.itamar.engine;


public enum LocType
{
    LAND,SEA,ALL;

    @Override
    public String toString() {
        switch (this){
            case SEA:
                return "S";
            case LAND:
                return "L";
            case ALL:
                return "A";
        }
        return "-";
    }
    public  boolean canStandHere(LocType other){
        if(other == this)
            return true;
        return this == ALL;
    }
}

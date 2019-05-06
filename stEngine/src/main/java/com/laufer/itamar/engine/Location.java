package com.laufer.itamar.engine;



import com.laufer.itamar.JsonParsable;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Location implements JsonParsable
{
    private int row;
    private int col;
    public static final Location OUT_LOCATION = new Location(-1, -1);
    private static final Location[][] LOCATIONS = new Location[20][20]; //Todo use constant
    static {
        for(int i=0; i < LOCATIONS.length; i++){
            for(int j=0; j < LOCATIONS.length; j++){
                LOCATIONS[i][j] = new Location(i, j);
            }
        }
    }


    private Location(int row, int col) {
        this.row = row;
        this.col = col;
    }


    public static boolean onSameRow(Location loc1, Location loc2){
        return loc1.row==loc2.row;
    }
    public boolean onSameRow(Location other){
        return Location.onSameRow(this,other);
    }
    public static boolean onSameCol(Location loc1,Location loc2){
        return loc1.col==loc2.col;
    }
    public boolean onSameCol(Location other){
        return Location.onSameCol(this,other);
    }
    public static boolean touches(Location loc1,Location loc2){
        if(onSameRow(loc1,loc2))
            return Math.abs(loc1.col-loc2.col)==1;
        if(onSameCol(loc1,loc2))
            return Math.abs(loc1.row-loc2.row)==1;
        return false;
    }
    public boolean touches(Location other){
        return Location.touches(this,other);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "Location{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
    public boolean notInBoard(int boardSize){
        return row < 0 || col < 0 || row >= boardSize || col >= boardSize;
    }

    /**
     * Calculates the locations that touches the square
     * @return list of the locations that touches the square
     */
    public List<Location>touchingLocations(int boardSize){
        List<Location>res = new ArrayList<>(4);
        res.add(generateLocation(row + 1, col));
        res.add(generateLocation(row - 1, col));
        res.add(generateLocation(row, col + 1));
        res.add(generateLocation(row, col +-1));
        res.removeIf(location -> location.notInBoard(boardSize));
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(!(obj instanceof Location))
            return false;
        return ((Location) obj).col == col && ((Location) obj).row == row;
    }
    public Location add(int rowAdd, int colAdd){
        return generateLocation(row + rowAdd, col + colAdd);
    }

    public JSONArray parseJson() {
        JSONArray res = new JSONArray();
        res.add(row);
        res.add(col);
        return res;
    }
    public static Location generateLocation(int row, int col){
        if(row >=0 && row < LOCATIONS.length && col >=0 && col < LOCATIONS.length)
            return LOCATIONS[row][col];
        return OUT_LOCATION;
    }

    public Location turned(int boardSize) {
        return generateLocation(boardSize - row - 1, col);
    }
}
package com.laufer.itamar.engine;



import java.util.ArrayList;
import java.util.List;

public class Location
{
    public static int BOARD_SIZE;
    private int row;
    private int col;

    public Location(int row, int col) {
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
    public boolean notInBoard(){
        return row < 0 || col < 0 || row >= BOARD_SIZE || col >= BOARD_SIZE;
    }

    /**
     * Calculates the locations that touches the square
     * @return list of the locations that touches the square
     */
    public List<Location>touchingLocations(){
        List<Location>res = new ArrayList<>(4);
        res.add(new Location(row + 1, col));
        res.add(new Location(row - 1, col));
        res.add(new Location(row, col + 1));
        res.add(new Location(row, col +-1));
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
        return new Location(row + rowAdd, col + colAdd);
    }
}

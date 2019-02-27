package com.laufer.itamar.engine.Loads;
import com.laufer.itamar.engine.Pieces.*;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.LoadVoidVisitor;

import java.util.ArrayList;
import java.util.List;
public abstract class Loads
{
    protected List<Soldier> soldiers;
    protected List<Bomb>bombs;
    protected List<Ship>ships;
    protected List<TourPlane>planes;
    private boolean hasFlag;
    private CanLoadVisitor canLoadVisitor;
    private LoadVoidVisitor loadVisitor;
    public Loads(){
        soldiers = new ArrayList<>();
        bombs=new ArrayList<>();
        ships = new ArrayList<>();
        planes = new ArrayList<>();
        canLoadVisitor = new CanLoadVisitor(this);
        loadVisitor = new LoadVoidVisitor(this);
        hasFlag = false;
    }
    public Boolean canLoad(Piece piece){
        return piece.accept(canLoadVisitor);
    }
    public abstract Boolean canLoad(LifeShip lifeShip);
    public abstract Boolean canLoad(Bomb bomb);
    public abstract Boolean canLoad(SpyShip spyShip);
    public abstract Boolean canLoad(Soldier soldier);
    public abstract Boolean canLoad(TourPlaneLoads tourPlaneLoads);
    public abstract Boolean canLoad(Flag flag);
    public Boolean canLoad(Ship ship){return false;} //cannot load ships that are not life or spy
    public Boolean canLoad(FighterPlane fighterPlane){return false;}//no one can load a fighter plane
    public void load(Piece piece){
       piece.accept(loadVisitor);
    }
    public void load(Soldier soldier){
        soldiers.add(soldier);
    }
    public void load(Bomb bomb){
        bombs.add(bomb);
    }
    public void load(Ship ship){
        ships.add(ship);
    }
    public void load(TourPlane tourPlane){
        planes.add(tourPlane);
    }
    public List<Piece>getAllLoads(){
        List<Piece>res = new ArrayList<>();
        res.addAll(soldiers);
        res.addAll(bombs);
        res.addAll(ships);
        res.addAll(planes);
        return res;
    }
    public int getAmountOfSoldiers() {
        int counter = 0;
        for (Soldier soldier : soldiers) {
            if (soldier.hasFlag())
                counter++;
            counter++;
        }
        return counter;
    }
    //Todo consider this
}

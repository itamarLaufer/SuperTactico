package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.engine.*;
import com.laufer.itamar.engine.Loads.Loads;
import com.laufer.itamar.engine.Movings.MoveType;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;
import com.laufer.itamar.engine.orders.MoveOrder;
import com.laufer.itamar.engine.orders.Order;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Piece
{

    protected int id;
    protected MoveType moveType;
    protected Location location;
    protected Player owner;
    protected SuperTacticoGame game;
    protected LocType locType;
    protected Loads loads;
    protected AttackVisitor attackVisitor;


    public Piece(SuperTacticoGame game, Location location, LocType locType, MoveType moveType, Loads loads, Player owner, int id){
        this.id = id;
        this.owner = owner;
        this.location = location;
        this.locType = locType;
        this.moveType = moveType;
        this.loads = loads;
        this.game = game;
        this.attackVisitor = new AttackVisitor(this);
        game.insertPiece(this);
    }
    public boolean canAttack(Piece other){
        if(!(location.touches(other.location)&&locType.canStandHere(other.locType))) //is not close enough or is on unreachable location?
            return false;
        if(owner == other.owner) //cannot attack pieces in his side
            return false;
        return true;
    }
    public AttackResult attack(Piece other){
        return other.accept(attackVisitor);
    }
    public abstract void accept(VoidVisitor voidVisitor);
    public abstract AttackResult accept(AttackVisitor attackVisitor);
    public abstract boolean accept(CanLoadVisitor canLoadVisitor);
    public boolean touches(Piece other){
        return Location.touches(location,other.location);
    }
    public boolean onSameRow(Piece other){
        return Location.onSameRow(location,other.location);
    }
    public boolean onSameCol(Piece other){
        return Location.onSameCol(location,other.location);
    }
    public boolean canLoad(Piece other){
        if(!touches(other))
            return false;
        if(getOwnerWhoCanLoad() != owner)
            return false;
        if(!other.getAllLoads().isEmpty() && !(other.getAllLoads().get(0) instanceof Flag)) //Todo find another way that is not instanceof
            return false;
        return loads.canLoad(other);
    }
    public void load(Piece toLoad){
        loads.load(toLoad);
    }

    public Player getOwnerWhoCanLoad() {
        return owner;
    }

    public LocType getLocType() {
        return locType;
    }

    public boolean basicCanMove(Location dest){
        if(location.equals(dest)) //it cannot move to it's current place
            return false;
        if(dest.notInBoard())
            return false;
        if(game.getPieceFromBoard(dest) != null) //square is taken
            return false;
        return locType.canStandHere(game.getLocTypeInLocation(dest));
    }
    public boolean canMove(Location dest){
        return moveType.getPossibleMoveLocations(this).contains(dest);
    }
    public List<Order>getPossibleOrders(){
        List<Order>orders = new LinkedList<>();
        orders.addAll(getPossibleMoveOrders());
        //Todo add attack load and unload orders

        return orders;
    }
    private List<MoveOrder> getPossibleMoveOrders(){
        return moveType.getPossibleMoveLocations(this).stream().map(it-> new MoveOrder(this, it)).collect(Collectors.toList());
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public void move(Location dest){
        for(Piece piece:getPieceAndItsLoads()){
            game.movePiece(piece,dest);
        }
    }
    public List<Piece>getPieceAndItsLoads(){
        List<Piece> res = getAllLoads();
        res.add(0,this);
        return res;
    }
    public List<Piece>getAllLoads(){
        return new ArrayList<>(loads.getAllLoads());
    }
    public abstract String getType();

    public void die() {
        boolean hasLifeSHip = false;
        for (Piece piece : getPieceAndItsLoads()) {
            if (piece instanceof LifeShip) { //Todo find another way with no instanceof
                hasLifeSHip = true;
                break;
            }
        }
        if (!hasLifeSHip) {
            game.removePiece(location);
            for (Piece piece : getPieceAndItsLoads()) {
                piece.die();
            }
        } else
            game.getDiedWithLifeShip().add(this);
    }
    public boolean unload(Piece piece, Location location){
        if(location.notInBoard() || !this.location.touches(location) || !loads.getAllLoads().contains(piece))
            return false;
        loads.getAllLoads().remove(piece);
        return true;
    }

    public AttackResult attack(Bomb bomb){
        die();
        return AttackResult.DEFEAT;
    }

    public Player getOwner() {
        return owner;
    }

    public SuperTacticoGame getGame() {
        return game;
    }

    public int getId() {
        return id;
    }
}

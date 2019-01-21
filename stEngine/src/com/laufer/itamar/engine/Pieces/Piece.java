package com.laufer.itamar.engine.Pieces;

import com.laufer.itamar.JsonParsable;
import com.laufer.itamar.engine.*;
import com.laufer.itamar.engine.Loads.Loads;
import com.laufer.itamar.engine.Movings.MoveType;
import com.laufer.itamar.engine.Visitors.AttackVisitor;
import com.laufer.itamar.engine.Visitors.CanLoadVisitor;
import com.laufer.itamar.engine.Visitors.VoidVisitor;
import com.laufer.itamar.engine.orders.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.laufer.itamar.engine.Location.generateLocation;

public abstract class Piece implements JsonParsable
{

    protected int id;
    protected MoveType moveType;
    protected Location location;
    protected Player owner;
    protected SuperTacticoGame game;
    protected LocType locType;
    protected Loads loads;
    protected AttackVisitor attackVisitor;
    protected Piece loader;


    public void setId(int id) {
        this.id = id;
    }

    public Piece(SuperTacticoGame game, Location location, LocType locType, MoveType moveType, Loads loads, Player owner, int id){
        this.id = id;
        this.owner = owner;
        this.location = location;
        this.locType = locType;
        this.moveType = moveType;
        this.loads = loads;
        this.game = game;
        this.attackVisitor = new AttackVisitor(this);
    }
    public boolean canAttack(Piece other){
        if(other == null || !(location.touches(other.location)&&locType.canStandHere(other.locType))) //is not close enough or is on unreachable location?
            return false;
        if(owner == other.owner) //cannot attack pieces in his side
            return false;
        return true;
    }
    public boolean canUnload(Location dest){
        return !dest.notInBoard(game.getBoardSize()) && this.location.touches(dest) && loader != null && locType.canStandHere(game.getLocTypeInLocation(dest));
    }
    public Boolean attack(Piece other){
        return other.accept(attackVisitor);
    }
    public abstract void accept(VoidVisitor voidVisitor);
    public abstract Boolean accept(AttackVisitor attackVisitor);
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
        if(other == null)
            return false;
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
        toLoad.loader = this;
    }

    public Player getOwnerWhoCanLoad() {
        return owner;
    }

    public LocType getLocType() {
        return locType;
    }

    public boolean basicCanMove(Location dest){
        if(dest == null || location.equals(dest)) //it cannot move to it's current place
            return false;
        if(dest.notInBoard(game.getBoardSize()))
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
        orders.addAll(getPossibleAttackOrders());
        orders.addAll(getPossibleLoadOrders());
        orders.addAll(getPossibleUnloadOrders());
        return orders;
    }
    private List<MoveOrder> getPossibleMoveOrders(){
        return moveType.getPossibleMoveLocations(this).stream().map(it-> new MoveOrder(this, it)).collect(Collectors.toList());
    }
    private List<AttackOrder> getPossibleAttackOrders(){
        return location.touchingLocations(game.getBoardSize()).stream().filter(it -> canAttack(game.getPieceFromBoard(it))).map(it -> new AttackOrder(this, it)).collect(Collectors.toList());
    }

    private List<LoadOrder> getPossibleLoadOrders(){
        return location.touchingLocations(game.getBoardSize()).stream().filter(it -> canLoad(game.getPieceFromBoard(it))).map(it -> new LoadOrder(this, it)).collect(Collectors.toList());
    }

    private List<UnloadOrder> getPossibleUnloadOrders(){
        return location.touchingLocations(game.getBoardSize()).stream().filter(this::canUnload).map(it -> new UnloadOrder(this, it)).collect(Collectors.toList());
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public void move(Location dest){
        for(Piece piece:getPieceAndItsLoads()){
            game.movePiece(piece, dest);
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
    public abstract int getType();

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
            owner.removePiece(this);
            location = Location.OUT_LOCATION;
            for (Piece piece : getPieceAndItsLoads()) {
                piece.die();
            }
        } else
            game.getDiedWithLifeShip().add(this);
    }
    public void unload(Location dest){
        move(dest);
        loader.loads.getAllLoads().remove(this);
        loader = null;
    }

    public Boolean attack(Bomb bomb){
        die();
        return false;
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

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Piece getLoader() {
        return loader;
    }
    public List<MoveOrder>getLocateOrders(){
        List<MoveOrder>res = new ArrayList<>(game.getBoardSize() * game.getBoardSize() / 2);
        for(int i=game.getBoardSize() - 1; i > 0.5 * game.getBoardSize(); i--){
            for(int j=0;j<game.getBoardSize();j++){
                if(locType.canStandHere(game.getLocTypeInLocation(i, j)) && game.getPieceFromBoard(i, j) == null)
                    res.add(new MoveOrder(this, generateLocation(i, j)));
            }
        }
        return res;
    }

    public JSONObject parseJson(String[]args) {
        if(args == null || args.length < 1)
            throw new IllegalArgumentException("Must receive argument for whether this piece should be represented as visible or invisible!");
        if(Integer.parseInt(args[0]) == owner.getId())
            return visibleParseJson(args);
        return invisibleParseJson(args);
    }
    public JSONObject invisibleParseJson(String[]args) {
        JSONObject res = new JSONObject();
        JSONArray arr = new JSONArray();
        for(Piece piece: getAllLoads()){
            arr.add(piece.parseJson(args));
        }
        res.put("id", id);
        res.put("loads", arr);
        res.put("location", location.parseJson());
        return res;
    }
    public JSONObject visibleParseJson(String[]args) {
        JSONObject res = invisibleParseJson(args);
        res.put("typeId", getType());
        return res;
    }
}

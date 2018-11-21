package com.laufer.itamar.communication.gui;

import com.laufer.itamar.engine.Location;
import com.laufer.itamar.engine.Pieces.Piece;
import com.laufer.itamar.engine.orders.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GuiPiece {
    private int id;
    private int owner;
    private String name;
    private Location location;
    private List<GuiPiece>loadingPieces;
    private static List<Order>EMPTY = new ArrayList<>(0);

    public GuiPiece(Piece piece, boolean visible) {
        id = piece.getId();
        owner = piece.getOwner().getId();
        name = visible ? piece.getType() : "unknown";
        location = piece.getLocation();
        loadingPieces = piece.getAllLoads().stream().map(it->new GuiPiece(it, visible)).collect(Collectors.toList());
    }
    public GuiPiece(Piece piece) {
        this(piece, true);
    }
}

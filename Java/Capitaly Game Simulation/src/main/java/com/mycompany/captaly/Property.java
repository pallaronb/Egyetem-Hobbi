/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.captaly;

/**
 *
 * @author palla
 */
public class Property extends FieldType {
    private boolean isOwned;
    private boolean hasHouse;
    private int tileNumber;
    private Player owner;
    /**
     * isOwned is whether the property is owned by someone
     * hasHouse is whether the property has a house on it
     * tileNumber is the number of the tile where the property is
     * owner is the Player who owns the property
     * @param tileNumber 
     */

    public Property(int tileNumber) {
        this.isOwned = false;
        this.hasHouse = false;
        this.tileNumber = tileNumber;
        this.owner = null;
    }

    public boolean isIsOwned() {
        return isOwned;
    }

    public boolean isHasHouse() {
        return hasHouse;
    }

    public int getTileNumber() {
        return tileNumber;
    }
    
    public Player getOwner(){
        return this.owner;
    }

    public void setIsOwned(boolean isOwned) {
        this.isOwned = isOwned;
    }

    public void setHasHouse(boolean hasHouse) {
        this.hasHouse = hasHouse;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
    
    @Override
    public void Act(Player p, Board board)
    {
        p.onProperty(this, board);
    }
    /**
     * to be dynamic
     */
}

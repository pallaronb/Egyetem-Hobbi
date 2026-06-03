/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.captaly;

/**
 *
 * @author palla
 */
public class Board {
    private FieldType[] fields;
    private int numOfEliminatedPlayers;
    private int numOfPlayers;
    /**
     * 
     * @param fields will be the fields of the board
     * @param numOfPlayers will be the number of players
     * numOfEliminatedPlayers will be the number of eliminated players
     */

    public Board(FieldType[] fields, int numOfPlayers) {
        this.fields = fields;
        this.numOfEliminatedPlayers = 0;
        this.numOfPlayers = numOfPlayers;
    }

    public void setNumOfEliminatedPlayers(int numOfEliminatedPlayers) {
        this.numOfEliminatedPlayers = numOfEliminatedPlayers;
    }
    
    public FieldType[] getFields() {
        return fields;
    }

    public int getNumOfEliminatedPlayers() {
        return numOfEliminatedPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

}

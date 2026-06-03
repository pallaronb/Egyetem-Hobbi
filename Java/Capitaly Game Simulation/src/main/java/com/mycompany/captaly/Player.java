/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.captaly;

import java.util.ArrayList;
/**
 *
 * @author palla
 */
public abstract class Player {
    private int money;
    private boolean alive;
    private ArrayList<Property> ownedProperties;
    private int onField;
    private ArrayList<Integer> diceRolls;
    private String name;
    /**
     * money is the amount the player currently has
     * alive is whether the player is alive
     * ownedProperties is a list of the owned properties
     * onField is the fields number the playetr is currently on
     * diceRolls is the rolls form the input, if there are any
     * name is the players name
     * @param diceRolls
     * @param name 
     */

    public Player(ArrayList<Integer> diceRolls, String name) {
        this.money = 10000;
        this.alive = true;
        this.ownedProperties = new ArrayList<Property>();
        this.onField = 0;
        this.diceRolls = diceRolls;
        this.name = name;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public boolean isAlive() {
        return alive;
    }

    public ArrayList<Property> getOwnedProperties() {
        return ownedProperties;
    }

    public int getOnField() {
        return onField;
    }

    public ArrayList<Integer> getDiceRolls() {
        return diceRolls;
    }
    
    public int diceRoll(){
        int rand = (int)(Math.random()* 6 + 1);
        return rand;
    }
    
    public void setOnField(int onField) {
        this.onField = onField;
    }
    
    public void buyProperty(Property p){
        ownedProperties.add(p);
        this.spendMoney(1000);
        p.setIsOwned(true);
        p.setOwner(this);
    }
    /**
     * The player buys property
     * Spends money
     * Gets ownership
     * @param p 
     */
    
    public void buildHouse(Property p){
        p.setHasHouse(true);
    }
    /**
     * The player builds a house on an owned property
     * @param alive 
     */

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
    
    public void payRent(Property p, Player t, Board board)
    {
        if(p.isHasHouse() && 2000 >= this.getMoney())
        {
            this.eliminatePlayer(board);
        }
        else if(500 >= this.getMoney())
        {
            this.eliminatePlayer(board);
        }else if(p.isHasHouse())
        {
            this.spendMoney(2000);
            t.recieveMoney(2000);
        }
        else
        {
            this.spendMoney(500);
            t.recieveMoney(500);
        }
    }
    /**
     * the player pays rent depending on the porperty attributes
     * If the player doesn't have the money for it, they get eliminated
     * @param s
     * @param board 
     */
    public void payServiceFee(Service s, Board board)
    {
        if(s.getMoney() >= this.getMoney())
        {
            this.eliminatePlayer(board);
        }else
        this.spendMoney(s.getMoney());
    }
    /**
     * The player pays Service fees
     * If doesn't has enough money, they get eliminated
     * @param m 
     */
    
    public void recieveMoney(int m)
    {
        this.setMoney(this.getMoney()+m);
    }
    /**
     * player recieves money
     * @param m 
     */
    
    public void spendMoney(int m){
        this.setMoney(this.getMoney()-m);
    }
    /**
     * Player spends money
     * @param board 
     */
    public void eliminatePlayer(Board board)
    {
        this.setAlive(false);
        for(Property r : this.getOwnedProperties()){
            r.setHasHouse(false);
            r.setIsOwned(false);
            r.setOwner(null);
        }
        board.setNumOfEliminatedPlayers(board.getNumOfEliminatedPlayers() + 1);
        if(board.getNumOfEliminatedPlayers() == 2)
        {
            System.out.println(this.name);
        }
        this.ownedProperties.clear();
    }
    /**
     * The player gets eliminated
     * owned propertys get disowned
     * houses get destroyed
     * increases the number of eliminated players
     * if that number equals 2, it prints out the players name
     * @param board
     * @param currentTurn 
     */
    
    public void playTurn(Board board, int currentTurn)
    {
        if(this.getDiceRolls().size() >= currentTurn)
        {
            this.setOnField((this.getOnField() + this.getDiceRolls().get(currentTurn-1)) % board.getFields().length);
        }else
        {
            this.setOnField(((this.getOnField() + this.diceRoll()) % board.getFields().length));
        }
        board.getFields()[this.getOnField()].Act(this, board);
    }
    /**
     * the player moves on the board, depending on inputted dicerolls
     * the landing field acts and does its own thing
     * @param p
     * @param board 
     */
    public abstract void onProperty(Property p, Board board);
}

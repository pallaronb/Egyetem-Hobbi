/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tron;

/**
 *
 * @author palla
 */
public class Menu {

    private Player playerOne;
    private Player playerTwo;
    /**
     * 
     * Adatok a játékosokról, bekérésük
    */
    public Menu() {
        playerOne = new Player(null,null,0,0,null);
        playerTwo = new Player(null,null,0,0,null);
        playerOne.setName("Player1");
        playerOne.setColor("CYAN");
        playerOne.setScore(0);
        playerOne.setAlive(true);
        playerTwo.setName("Player2");
        playerTwo.setColor("MAGENTA");
        playerTwo.setScore(0);
        playerTwo.setAlive(true);
    }

    public void changePlayerName(Player p, String name){
        p.setName(name);
    }
    public void changeColor(Player p, String color) {
        p.setColor(color);
    }
    public Player getPlayerOne() {
        return playerOne;
    }
    public Player getPlayerTwo() {
        return playerTwo;
    }
    public Player[] getPlayers() {
        return new Player[]{playerOne, playerTwo};
    }
}

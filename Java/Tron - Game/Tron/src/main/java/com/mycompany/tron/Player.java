/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tron;

import java.awt.event.KeyEvent;

/**
 *
 * @author palla
 */
public class Player {

    private String name;
    private int score;
    private String color;
    private int xPos;
    private int yPos;
    private boolean alive;
    private String direction;
    private String[][] trail;

    public Player(String name, String color, int xPos, int yPos, String direction) {
        this.name = name;
        this.score = 0;
        this.color = color;
        this.xPos = xPos;
        this.yPos = yPos;
        this.alive = true;
        this.direction = direction;
        this.trail = new String[100][100];
    }

    public void addTrail(int x, int y) {
        this.trail[x][y] = this.name;
    }

    public String getTile(int x, int y) {
        return this.trail[x][y];
    }

    public String[][] getTrail() {
        return trail;
    }

    public void setTile(String name, int xPos, int yPos) {
        this.trail[xPos][yPos] = name;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public String getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }
    /**
    A játékos tényleges léptetése
    */
    public void step() {
        switch (this.direction) {
            case "up" ->
                this.setyPos(this.getyPos() - 1);
            case "down" ->
                this.setyPos(this.getyPos() + 1);
            case "left" ->
                this.setxPos(this.getxPos() - 1);
            case "right" ->
                this.setxPos(this.getxPos() + 1);
        }
    }

    /**
    Az irányítógombok lenyomására átadjok a kanyarodási kérelmet
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case 'w':
                this.turn("up");
                break;
            case 's':
                this.turn("down");
                break;
            case 'a':
                this.turn("left");
                break;
            case 'd':
                this.turn("right");
                break;
            case KeyEvent.VK_UP:
                this.turn("up");
                break;
            case KeyEvent.VK_DOWN:
                this.turn("down");
                break;
            case KeyEvent.VK_LEFT:
                this.turn("left");
                break;
            case KeyEvent.VK_RIGHT:
                this.turn("right");
                break;
            default:
                break;
        }
    }

    /**
    Ténylegesen kanyarodik a keyPressed-el való együttműködésnek köszönhetően
     */
    public void turn(String dir) {
        switch (dir) {
            case "up":
                if (!this.direction.equals("down")) {
                    this.direction = dir;
                }
                break;
            case "left":
                if (!this.direction.equals("right")) {
                    this.direction = dir;
                }
                break;
            case "right":
                if (!this.direction.equals("left")) {
                    this.direction = dir;
                }
                break;
            case "down":
                if (!this.direction.equals("up")) {
                    this.direction = dir;
                }
                break;
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tron;


/**
 *
 * @author palla
 */
public class Game {

    private String[][] gameBoard;
    private int timeCounter;
    private Player players[];
    private boolean running;
    /**
    Létrehozzuk a játék logikáját a menüben megadott adatokból
    */
    public Game(Menu menu, LeaderboardGUI lb) {
        this.players = new Player[2];
        players[0] = menu.getPlayerOne();
        players[1] = menu.getPlayerTwo();
        players[0].setxPos(25);
        players[0].setyPos(50);
        players[0].setDirection("right");
        players[0].setAlive(true);
        players[1].setxPos(75);
        players[1].setyPos(50);
        players[1].setDirection("left");
        players[1].setAlive(true);
        running = true;
        int numOfBarriers = (int) (Math.random() * 51);
        this.gameBoard = new String[100][100];
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                gameBoard[y][x] = "None";
            }
        }
        int horizontal = 0;
        for (int g = 0; g < numOfBarriers; g++) {
            int whereY = (int) (Math.random() * 91);
            int whereX = (int) (Math.random() * 91);
            int lengthOfBarriers = (int) (Math.random() * 20);
            for (int j = 0; j < lengthOfBarriers; j++) {
                int bx, by;
                if (horizontal % 2 == 0) {
                    bx = whereX;
                    by = whereY + j;
                } else {
                    bx = whereX + j;
                    by = whereY;
                }
                if (bx < 0 || bx >= 100 || by < 0 || by >= 100) {
                    continue;
                }
                if (isInSafeZone(bx, by, players[0]) || isInSafeZone(bx, by, players[1])) {
                    continue;
                }
                gameBoard[by][bx] = "barrier";
            }
            horizontal++;
        }
    }

    public Player[] getPlayers() {
        return players;
    }
    /**
    Reseteljük a pálya logikáját
    */
    public void reset() {
        this.gameBoard = new String[100][100];
        this.timeCounter = 0;
        int numOfBarriers = (int) (Math.random() * 51);
        this.gameBoard = new String[100][100];
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                gameBoard[y][x] = "None";
            }
        }
        int horizontal = 0;
        for (int g = 0; g < numOfBarriers; g++) {
            int whereY = (int) (Math.random() * 91);
            int whereX = (int) (Math.random() * 91);
            int lengthOfBarriers = (int) (Math.random() * 20);
            for (int j = 0; j < lengthOfBarriers; j++) {
                int bx, by;
                if (horizontal % 2 == 0) {
                    bx = whereX;
                    by = whereY + j;
                } else {
                    bx = whereX + j;
                    by = whereY;
                }
                if (bx < 0 || bx >= 100 || by < 0 || by >= 100) {
                    continue;
                }
                if (isInSafeZone(bx, by, players[0]) || isInSafeZone(bx, by, players[1])) {
                    continue;
                }
                gameBoard[by][bx] = "barrier";
            }
            horizontal++;
        }
        gameBoard[50][25] = players[0].getName();
        gameBoard[50][75] = players[1].getName();
    }
    /**
    Ez számít egy lépésnek, egy irányba való elmenésnek logikailag
    */
    public void tick(LeaderboardGUI lb) {
        if (!running) {
            return;
        }
        for (Player p : players) {
            if (p.isAlive()) {
                p.step();
            }
        }
        for (int i = 0; i < players.length; i++) {
            Player p = players[i];
            if (!p.isAlive()) {
                continue;
            }
            if (collides(p)) {
                p.setAlive(false);
                running = false;
                int other = (i == 0) ? 1 : 0;
                players[other].setScore(lb.getPlayerScore(players[other].getName()) + 1);
                lb.updateLeaderboard(players[other].getName(),players[other].getScore());
                lb.updateLeaderboard(players[i].getName(),players[i].getScore());
                return;
            }
        }
        for (Player p : players) {
            if (p.isAlive()) {
                int x = p.getxPos();
                int y = p.getyPos();
                gameBoard[y][x] = p.getName();
                p.addTrail(x, y);
            }
        }
    }
    /**
    Nézzük, hogy nem ütközött-e a játékos
    */
    public boolean collides(Player p) {
        int x = p.getxPos();
        int y = p.getyPos();
        if (x < 0 || x >= 100 || y < 0 || y >= 100) {
            return true;
        }
        if (!gameBoard[y][x].equals("None")) {
            return true;
        }
        return false;
    }
    /**
    Új játék indítása logikailag
    */
    public void newGame() {
        int numOfBarriers = (int) (Math.random() * 51);
        this.gameBoard = new String[100][100];
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                gameBoard[y][x] = "None";
            }
        }
        int horizontal = 0;
        for (int g = 0; g < numOfBarriers; g++) {
            int whereY = (int) (Math.random() * 91);
            int whereX = (int) (Math.random() * 91);
            int lengthOfBarriers = (int) (Math.random() * 20);
            for (int j = 0; j < lengthOfBarriers; j++) {
                int bx, by;
                if (horizontal % 2 == 0) {
                    bx = whereX;
                    by = whereY + j;
                } else {
                    bx = whereX + j;
                    by = whereY;
                }
                if (bx < 0 || bx >= 100 || by < 0 || by >= 100) {
                    continue;
                }
                if (isInSafeZone(bx, by, players[0]) || isInSafeZone(bx, by, players[1])) {
                    continue;
                }
                gameBoard[by][bx] = "barrier";
            }
            horizontal++;
        }
        players[0].setAlive(true);
        players[1].setAlive(true);
        players[0].setxPos(25);
        players[0].setyPos(50);
        players[1].setxPos(75);
        players[1].setyPos(50);
        players[0].setDirection("right");
        players[1].setDirection("left");
        running = true;
    }
    /**
    Azért, hogy a játékosok ne halhassanak meg egyből, ezért 10-es körzetükben nem lehetnek barrierek
    */
    private boolean isInSafeZone(int x, int y, Player p) {
        return Math.abs(p.getxPos() - x) <= 10 && Math.abs(p.getyPos() - y) <= 10;
    }

    public String[][] getGameBoard() {
        return gameBoard;
    }

    public int getTimeCounter() {
        return timeCounter;
    }

    public void setGameBoard(String[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void setTimeCOunter(int timeCounter) {
        this.timeCounter = timeCounter;
    }
    public boolean isRunning() {
        return running;
    }
}

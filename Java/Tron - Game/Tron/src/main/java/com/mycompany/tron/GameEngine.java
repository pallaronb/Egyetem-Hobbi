package com.mycompany.tron;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameEngine extends JPanel {
    
    private static final int GRID_SIZE = 100;
    private static final int CELL_SIZE = 10;
    private final int HEIGHT = GRID_SIZE * CELL_SIZE;
    private final int FPS = 10;
    
    private Game game;
    private LeaderboardGUI lb;
    private Timer gameLoopTimer;
    /**
     * Beállítjuk az alap privát adattagokat
     * @param game
     * @param lb 
     */
    public GameEngine(Game game, LeaderboardGUI lb) {
        this.game = game;
        this.lb = lb;
        int WIDTH = GRID_SIZE * CELL_SIZE;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new TronKeyAdapter());
        gameLoopTimer = new Timer(1000 / FPS, new GameLoopListener());
        gameLoopTimer.start();
    }
    /**
     * Segédfüggvény, egy szövegből kiszedjük a tényleges színt
     * @param name
     * @return 
     */
    private Color getColorFromName(String name) {
        return switch (name.toUpperCase()) {
            case "RED" -> Color.RED;
            case "GREEN" -> Color.GREEN;
            case "BLUE" -> Color.BLUE;
            case "CYAN" -> Color.CYAN;
            case "YELLOW" -> Color.YELLOW;
            case "MAGENTA" -> Color.MAGENTA;
            case "ORANGE" -> Color.ORANGE;
            case "PINK" -> Color.PINK;
            default -> Color.WHITE;
        };
    }
    /**
     * Lerajzoljuk a g objektumot
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
    }
    /**
     * Lerajzoljuk a játékpályát, valamint a játékosokat, csíkuk és jelenlegi pozicíójuk alapján
     * @param g 
     */
    private void drawGame(Graphics g) {
        String[][] board = game.getGameBoard();
        Player[] players = game.getPlayers();
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                String cellContent = board[y][x];
                Color cellColor = Color.DARK_GRAY;
                if (cellContent.equals("barrier")) {
                    cellColor = Color.YELLOW;
                } else if (cellContent.equals(players[0].getName())) {
                    cellColor = getColorFromName(players[0].getColor());
                } else if (cellContent.equals(players[1].getName())) {
                    cellColor = getColorFromName(players[1].getColor());
                }
                g.setColor(cellColor);
                g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }
    /**
     * Minden egyes loopnál újrarajzolunk
     */
    class GameLoopListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            game.tick(lb);
            repaint();
        }
    }
    /**
     * Mozgások beállítása
     */
    class TronKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            Player p1 = game.getPlayers()[0];
            Player p2 = game.getPlayers()[1];
            if (e.getKeyChar() == 'w' || e.getKeyChar() == 'W') p1.turn("up");
            if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') p1.turn("down");
            if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') p1.turn("left");
            if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') p1.turn("right");
            if (e.getKeyCode() == KeyEvent.VK_UP) p2.turn("up");
            if (e.getKeyCode() == KeyEvent.VK_DOWN) p2.turn("down");
            if (e.getKeyCode() == KeyEvent.VK_LEFT) p2.turn("left");
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) p2.turn("right");
        }
    }
}
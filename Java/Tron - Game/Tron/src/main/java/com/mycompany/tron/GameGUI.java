package com.mycompany.tron;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameGUI extends JFrame {

    private Game game;
    private GameEngine gamePanel;
    private Timer checkEndGameTimer;

    public GameGUI(Menu menu, LeaderboardGUI lb) {
        this.game = new Game(menu, lb);
        Player[] ps = game.getPlayers();
        setTitle("Tron – The Grid");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gamePanel = new GameEngine(game, lb);
        this.add(gamePanel, BorderLayout.CENTER);
        JPanel controlPanel = new JPanel();
        JButton restartButton = new JButton("New Game");
        JButton backToMenuButton = new JButton("Back to menu");
        backToMenuButton.addActionListener(e -> backToMenu());
        restartButton.addActionListener(e -> startNewGame());
        controlPanel.add(backToMenuButton);
        controlPanel.add(restartButton);
        this.add(controlPanel, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        gamePanel.requestFocusInWindow();
        checkEndGameTimer = new Timer(500, new ActionListener() {
            @Override
            /**
            Ha vége van a játéknak, akkor kiírjuk a győzteset / döntetlent
            */
            public void actionPerformed(ActionEvent e) {
                if (!game.isRunning()) {
                    checkEndGameTimer.stop();
                    String message = "Draw!";
                    if (ps[0].isAlive()) {
                        message = ps[0].getName() + " wins!";
                    } else {
                        message = ps[1].getName() + " wins!";
                    }
                    JOptionPane.showMessageDialog(GameGUI.this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        checkEndGameTimer.start();
    }
    /**
    Visszalépés a menübe
    */
    private void backToMenu() {
        this.dispose();
        new MenuGUI();
    }
    /**
    Játékból való, új játék indítása
    */
    private void startNewGame() {
        game.newGame();
        gamePanel.repaint();
        gamePanel.requestFocusInWindow();
        if (!checkEndGameTimer.isRunning()) {
            checkEndGameTimer.start();
        }
    }
}

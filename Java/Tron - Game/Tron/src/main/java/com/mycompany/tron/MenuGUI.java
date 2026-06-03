/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tron;

import java.awt.GridLayout;
import javax.swing.*;

/**
 *
 * @author palla
 */
public class MenuGUI extends JFrame {

    private Menu menu;
    private LeaderboardGUI lb;

    private JTextField p1Name;
    private JTextField p2Name;

    private JComboBox<String> p1Color;
    private JComboBox<String> p2Color;
    /**
     * Adatok bekérésének vizualizálása
    */
    public MenuGUI() {
        this.menu = new Menu();
        this.lb = new LeaderboardGUI();
        setTitle("Tron – Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10));
        add(new JLabel("Player name:"));
        p1Name = new JTextField();
        add(p1Name);
        add(new JLabel("Player color:"));
        p1Color = new JComboBox<>(new String[]{"CYAN", "GREEN", "RED", "YELLOW"});
        add(p1Color);
        add(new JLabel("Player name:"));
        p2Name = new JTextField();
        add(p2Name);
        add(new JLabel("Player color:"));
        p2Color = new JComboBox<>(new String[]{"MAGENTA", "ORANGE", "BLUE", "PINK"});
        add(p2Color);
        JButton startButton = new JButton("Start game");
        JButton leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.addActionListener(e -> lb.showLeaderboard());
        add(leaderboardButton);
        startButton.addActionListener(e -> startGame());
        add(new JLabel());
        add(startButton);
        setVisible(true);
    }
    /**
     * Játék indítása vizuálisan
     */
    private void startGame() {
        if (p1Name.getText().isEmpty()) {
            menu.getPlayerOne().setName("Player 1");
        }else{
            menu.getPlayerOne().setName(p1Name.getText());
        }
        if (p2Name.getText().isEmpty()) {
            menu.getPlayerTwo().setName("Player 2");
        }else{
            menu.getPlayerTwo().setName(p2Name.getText());
        }
        menu.getPlayerOne().setColor((String) p1Color.getSelectedItem());
        menu.getPlayerTwo().setColor((String) p2Color.getSelectedItem());
        new GameGUI(menu, lb);
        dispose();
    }
}

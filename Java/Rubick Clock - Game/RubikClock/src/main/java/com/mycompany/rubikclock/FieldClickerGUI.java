/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rubikclock;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author palla
 */

public class FieldClickerGUI extends JFrame{
    private Board board = new Board();
    private BoardGUI boardGUI;
    
    private JLabel moveLabel = new JLabel("Moves: 0");
    
    public FieldClickerGUI(){
        super("Rubik-Clocks");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));
        JPanel top = new JPanel();
        JButton newGame = new JButton("New Game!");
        newGame.addActionListener(e -> newGame());
        top.add(moveLabel);
        top.add(newGame);
        add(top, BorderLayout.NORTH);
        pack();
        setSize(700,700);
        setVisible(true);
        boardGUI = new BoardGUI(board, moves -> moveLabel.setText("Moves: " + moves));
        add(boardGUI, BorderLayout.CENTER);
    }
    public interface MoveListener {
        void onMoveCountChanged(int moves);
    }
    
    public void newGame() {
        board.reset();
        board.setMoves(0);
        boardGUI.refreshAll();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rubikclock;
import com.mycompany.rubikclock.FieldClickerGUI.MoveListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author palla
 */
public class BoardGUI extends JPanel {

    private Board board;
    private JButton[][] buttons = new JButton[5][5];
    private JLabel[][] clocks = new JLabel[5][5];
    private MoveListener moveListener;

    
    public BoardGUI(Board board, MoveListener moveListener) {
        this. moveListener = moveListener;
        this.board = board;
        setLayout(new GridLayout(5,5,5,5));
        for(int i = 0; i<5;i++)
        {
            for(int j = 0; j<5; j++){
                if(i%2==0 && j%2 == 0){
                    JLabel clock = new JLabel();
                    clock.setPreferredSize(new Dimension(60,60));
                    clock.setFont(new Font("Times New Roman",Font.BOLD, 22));
                    clock.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,2));
                    clock.setHorizontalAlignment(SwingConstants.CENTER);
                    clock.setVerticalAlignment(SwingConstants.CENTER);
                    clocks[i][j] = clock;
                    add(clock);
                }
                else if(i % 2 == 1 && j % 2 == 1)
                {
                    JButton button = getQuadrantFromPosition(i,j);
                    button.setPreferredSize(new Dimension(40,40));
                    button.setOpaque(true);
                    button.setBackground(Color.DARK_GRAY);
                    buttons[i][j]=button;
                    add(button);
                }
                else{
                    JLabel filler = new JLabel();
                    filler.setOpaque(true);
                    filler.setBackground(Color.WHITE);
                    add(filler);
                }
            }
        }
        refreshAll();
    }

    public void refreshAll() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(i % 2 == 0 && j % 2 == 0)
                {
                    clocks[i][j].setText(String.valueOf(board.getField(i, j).getValue()));
                }
            }
        }
        moveListener.onMoveCountChanged(board.getMoves());
    }

    public void newGame() {
        board.reset();
        board.setMoves(0);
        refreshAll();
    }
    public JButton createQuadrantButton(String quadrant)
    {
        JButton button= new JButton();
        button.setPreferredSize(new Dimension(40,40));
        button.addActionListener(e -> handleQuadrantPress(quadrant));
        return button;
    }
    
    public JButton getQuadrantFromPosition(int x, int y)
    {
        if(x==1 && y == 1) return createQuadrantButton("TL");
        if(x==1 && y == 3) return createQuadrantButton("TR");
        if(x==3 && y == 1) return createQuadrantButton("BL");
        if(x==3 && y == 3) return createQuadrantButton("BR");
        return null;
    }
    public void handleQuadrantPress(String quadrant) {
        board.quadrantPressed(quadrant);
        board.setMoves(board.getMoves() + 1);
        refreshAll();

        if (board.isOver()) {
            JOptionPane.showMessageDialog(this,
                    "YOU WIN! with " + board.getMoves() + " moves!!",
                    "Done!", JOptionPane.INFORMATION_MESSAGE);
            board.reset();
            refreshAll();
        }
}
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package citybuilder;

import java.awt.*;
import javax.swing.*;


public class MenuGUI extends JFrame {

    public MenuGUI() {
        setTitle("ClosedAAD - Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        BackgroundPanel mainPanel = new BackgroundPanel(getClass().getResource("/Bg.jpg"));
        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        JButton startButton = createMenuButton("New Game");
        JButton loadButton = createMenuButton("Load Game");
        JButton exitButton = createMenuButton("Exit");
        startButton.addActionListener(e -> startGame());
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(startButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(exitButton);
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        add(buttonPanel);
        setVisible(true);
    }

    private JButton createMenuButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(new Color(169, 169, 169));
        b.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(Color.GRAY);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(Color.LIGHT_GRAY);
            }
        });
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    static class BackgroundPanel extends JPanel {
        private Image bgImage;
        public BackgroundPanel(java.net.URL imageUrl) {
            if (imageUrl != null) {
                bgImage = new ImageIcon(imageUrl).getImage();
            } else {
                System.err.println("CRITICAL: Cannot find Bg.jpg in the resources folder!");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) {
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    private void startGame() {
        Player player = new Player();
        Map map = new Map(100, 100);
        map.createMap();
        TimeHandler timer = new TimeHandler();
        new GameGUI(player, map, timer);
        dispose();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.tron;

import javax.swing.SwingUtilities;

/**
 *
 * @author palla
 */
public class Tron {
    /**
    Main, elindítjuk a menüt
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuGUI();
        });
    }
}

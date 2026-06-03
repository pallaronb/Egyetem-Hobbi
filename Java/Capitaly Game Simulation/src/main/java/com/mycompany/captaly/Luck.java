/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.captaly;

/**
 *
 * @author palla
 */
public class Luck extends FieldType {
    private int money;
    public Luck(int money)
    {
        this.money = money;
    }
    /**
     * money will be the value we get from Lucky tiles
     * @return 
     */

    public int getMoney() {
        return money;
    }
    @Override
    public void Act(Player p, Board board)
    {
        p.recieveMoney(this.getMoney());
    }
    /**
     * We just put it to recieveMoney method
     */
}

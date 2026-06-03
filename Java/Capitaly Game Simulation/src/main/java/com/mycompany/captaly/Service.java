/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.captaly;

/**
 *
 * @author palla
 */
public class Service extends FieldType {
    private int money;
    public Service(int money)
    {
        this.money = money;
    }
    /**
     * money will be the amount the player has to pay
     * @return 
     */

    public int getMoney() {
        return money;
    }
    @Override
    public void Act(Player p, Board board)
    {
        p.payServiceFee(this, board);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.captaly;

import java.util.ArrayList;
/**
 *
 * @author palla
 */
public class Careful extends Player {
    private int moneySpentThisRound;
    public Careful(ArrayList<Integer> dicerolls, String name)
    {
        super(dicerolls, name);
        this.moneySpentThisRound = 0;
    }

    public int getMoneySpentThisRound() {
        return moneySpentThisRound;
    }
    @Override
    public void onProperty(Property p, Board board)
    {
        if(p.getOwner() != this && p.isIsOwned())
        {
            this.payRent(p, p.getOwner(), board);
        }
        else if(!p.isIsOwned())
        {
            int halfmoney = this.getMoney() / 2;
            if(this.moneySpentThisRound+1000 < halfmoney && this.getMoney() > 1000)
            {
                this.buyProperty(p);
            }
        }
        else if(p.getOwner() == this && !p.isHasHouse())
        {
            int halfmoney = this.getMoney() / 2;
            if(this.moneySpentThisRound+4000 < halfmoney && this.getMoney() > 4000)
            {
                this.buildHouse(p);
            }
        }
    }
    /**
     * If careful is on property we count how much money it spent in this turn
     * depending on spent money, it can buy properties, build houses, and pay rent
     */
}

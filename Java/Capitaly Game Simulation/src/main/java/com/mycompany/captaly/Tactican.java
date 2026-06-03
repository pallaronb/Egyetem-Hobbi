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
public class Tactican extends Player {
    private int buyOpportunities;
    public Tactican(ArrayList<Integer> dicerolls, String name)
    {
        super(dicerolls, name);
        this.buyOpportunities = 0;
    }

    public int getBuyOpportunities() {
        return buyOpportunities;
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
            if(buyOpportunities % 2 == 0 && this.getMoney() > 1000)
            {
                this.buyProperty(p);
            }
            this.buyOpportunities += 1;
        }
        else if(p.getOwner() == this)
        {
            if(this.buyOpportunities % 2 == 0 && this.getMoney() > 4000 && !p.isHasHouse())
            {
                this.buildHouse(p);
            }
            this.buyOpportunities +=1;
        }
    }
    /**
     * Tactican gets on property
     * pays rent or depending on the number of buying opportunities builds house or buys property
     */
}

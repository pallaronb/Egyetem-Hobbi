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
public class Greedy extends Player {
    public Greedy(ArrayList<Integer> dicerolls, String name)
    {
        super(dicerolls,name);
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
            if(this.getMoney() > 1000)
            {
                this.buyProperty(p);
            }
        }
        else if(p.getOwner() == this && !p.isHasHouse())
        {
            if(this.getMoney() > 4000)
            {
                this.buildHouse(p);
            }
        }
    }
    /**
     * Greedy player gets on property
     * If it can it will buy / build everything
     * pays rent if it can
     */
}
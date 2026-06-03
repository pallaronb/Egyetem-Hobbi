/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rubikclock;

/**
 *
 * @author palla
 */
public class Field {
    private int value;

    public Field(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int newValue) {
        this.value = newValue;
    }
    public void increase()
    {
        if(this.getValue() == 12){
            this.setValue(1);
        }
        else{
            this.setValue(this.getValue()+1);
        }
        
    }
    public void randomize()
    {
        this.setValue((int)(Math.random()*12) + 1);
    }
    public boolean isSolved(){
        return this.getValue() == 12;
    }
}
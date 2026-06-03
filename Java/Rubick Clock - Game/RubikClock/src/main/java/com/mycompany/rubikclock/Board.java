/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rubikclock;

/**
 *
 * @author palla
 */
public class Board {
    private Field[][] fields;
    private int moves;

    public Board() {
        fields = new Field[5][5];
        for(int i = 0; i<5;i++){
            for(int j = 0; j<5; j ++){
                if(i % 2 == 0 && j % 2 == 0){
                    fields[i][j] = new Field((int)(Math.random() * 12) + 1);
                }
            }
        }
        this.moves=0;
    }
    
    public void reset(){
        for(int i = 0; i<5;i++){
            for(int j = 0; j<5; j ++){
                if(i % 2 == 0 && j % 2 == 0){
                    fields[i][j].randomize();
                }
            }
        }
        this.setMoves(0);
    }

    public Field[][] getFields() {
        return fields;
    }
    public Field getField(int x, int y){
        return getFields()[x][y];
    }
    public int getMoves() {
        return moves;
    }

    public void setFields(Field[][] fields) {
        this.fields = fields;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }
    public boolean isOver()
    {
        for(int i = 0; i<5;i++){
            for(int j = 0; j<5; j ++){
                if(i % 2 == 0 && j % 2 == 0){
                    if(fields[i][j].getValue()!=12){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public void quadrantPressed(String q){
        int[][] coords;
        switch(q){
            case "TL" -> coords = new int[][]{{0,0},{2,2},{0,2},{2,0}};
            case "TR" -> coords = new int[][]{{0,2},{0,4},{2,2},{2,4}};
            case "BL" -> coords = new int[][]{{2,0},{2,2},{4,0},{4,2}};
            case "BR" -> coords = new int[][]{{2,2},{2,4},{4,2},{4,4}};
            default -> {return;}
        }
        for(int[] pos: coords){
            fields[pos[0]][pos[1]].increase();
        }
    }
}

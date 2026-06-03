/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.captaly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author palla
 */
class Captaly {
    public static void main(String[] args) throws FileNotFoundException, UnexpectedInputException, IOException
    {
        /**
         * Expected file format
         * "Length of Board"
         * "P" (Property)
         * "L (number)" (Luck)
         * "S (number)" (Service)
         * "Number of players"
         * "(player name) (player strategy's first letter in caps) (dicerolls)"
         * ...
         * End of File
         */
        try
        {
            /**
             * We read from the input and check for the file!
             */
            System.out.print("Entrer filename: ");
            BufferedReader r = new BufferedReader(
            new InputStreamReader(System.in));
            String s = r.readLine();
            Scanner sc = new Scanner(new File(s));
            int length = sc.nextInt();
            sc.nextLine();
            String[] fields = new String[length];
            /**
             * We fill in two temporary arrays
             * fields -> later will fields for the Board
             * playerAttributes -> later will be players names, strategies and dicerolls
             */
            for(int i =0; i< length; i++)
            {
              fields[i] = sc.nextLine();
            }
            int numOfPlayers = sc.nextInt();
            sc.nextLine();
            String[] playerAttributes = new String[numOfPlayers];
            for(int i = 0; i<numOfPlayers; i++)
            {
                playerAttributes[i] = sc.nextLine();
            }
            /**
             * We conver everything!
             */
            FieldType[] tiles = new FieldType[length];
            for(int i = 0; i<length; i++)
            {
                String[] splitted = fields[i].split("\\s");
                switch(splitted[0])
                {
                    case "L":
                        tiles[i] = new Luck(parseInt(splitted[1]));
                        break;
                    case "S":
                        tiles[i] = new Service(parseInt(splitted[1]));
                        break;
                    case "P":
                        tiles[i] = new Property(i);
                        break;
                    default:
                        throw new UnexpectedInputException();
                }
            }
            Player[] players= new Player[numOfPlayers];
            for(int i = 0; i< numOfPlayers; i++)
            {
                ArrayList<Integer> diceRolls = new ArrayList<>();
                String[] splitted = playerAttributes[i].split("\\s+");
                if(splitted.length > 2)
                {
                    for(int j = 2; j< splitted.length; j++)
                    {
                        diceRolls.add(Integer.parseInt(splitted[j]));
                    }
                }
                switch(splitted[1]){
                    case "C":
                        players[i] = new Careful(diceRolls, splitted[0]);
                        break;
                    case "G":
                        players[i] = new Greedy(diceRolls, splitted[0]);
                        break;
                    case "T":
                        players[i] = new Tactican(diceRolls, splitted[0]);
                        break;
                    default:
                        throw new UnexpectedInputException();
                }
            }
            /**
             * Create Board and simulate until two players got eliminated
             */
            Board board = new Board(tiles, numOfPlayers);
            int currentTurn = 1;
            while(board.getNumOfEliminatedPlayers() < 2)
            {
                for(int i = 0; i < numOfPlayers; i++)
                {
                    if(players[i].isAlive())
                    {
                        players[i].playTurn(board, currentTurn);
                    }
                }
                currentTurn += 1;
            }
        }catch(FileNotFoundException e)
        {
            System.out.println("File not found!");
            
        }
    }
}

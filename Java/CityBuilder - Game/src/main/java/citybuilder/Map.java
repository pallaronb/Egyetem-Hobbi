package citybuilder;

import citybuilder.map.materials.*;
import citybuilder.map.tile.*;

import java.util.ArrayList;
import java.util.Random;

public class Map {
    private final int vertical;
    private final int horizontal;
    private final Tile[][] map;
    private final String[][] stringMap;
    private NoiseGenerator noiseGen;
    private long seed;
    private ForestGrower fg;
    private final ArrayList<Factory> factories;
    private final ArrayList<City> cities;
    public Map(int x, int y){
        this.vertical = y;
        this.horizontal = x;
        this.map = new Tile[x][y];
        this.stringMap = new String[x][y];
        this.seed = System.currentTimeMillis();
        this.noiseGen = new NoiseGenerator(seed);
        this.fg = new ForestGrower(this);
        this.factories = new ArrayList<>();
        this.cities = new ArrayList<>();
    }
    public void setStringMapTile(int x,int y, String newTile){
        this.stringMap[x][y] = newTile;
    }

    public ForestGrower getFg(){
        return this.fg;
    }

    public void createMap() {
        double frequency = 0.07;

        for (int x = 0; x < this.horizontal; x++) {
            for (int y = 0; y < this.vertical; y++) {
                if (this.map[x][y] != null || this.stringMap[x][y] != null) continue;
                double val = (noiseGen.noise(x * frequency, y * frequency) + 1) / 2.0;
                if (val < 0.4) {
                    int[] pos = {x, y};
                    this.map[x][y] = new River(pos);
                    this.stringMap[x][y] = "RIVER";
                }
                else if(val >= 0.4 && val < 0.5){
                    if(Math.random() < 0.008){
                        generateFactory(x, y);
                    }else{
                        int[] pos = {x, y};
                        this.map[x][y] = new Plains(pos);
                        this.stringMap[x][y] = "PLAINS";
                    }
                }
                else if (val >= 0.5 && val < 0.6) {
                    if (Math.random() < 0.003){
                        generateCity(x, y);
                    }
                    else{
                        int[] pos = {x, y};
                        this.map[x][y] = new Plains(pos);
                        this.stringMap[x][y] = "PLAINS";
                    }
                }
                else if (val >= 0.6) {
                    generateForest(x, y);
                }
                else {
                    int[] pos = {x, y};
                    this.map[x][y] = new Plains(pos);
                    this.stringMap[x][y] = "PLAINS";
                }
            }
        }
    }

    private void generateForest(int x, int y){
        int verticalSize = (int)(Math.random() * 6);
        int horizontalSize = (int)(Math.random() * 6);
        int[] position = {x, y};
        int rnd = (int) (Math.random() * 5);
        fg.addForestCoord(position);
        switch(rnd){
            case 2:
                this.stringMap[x][y] = "TREE2";
                this.map[x][y] = new Forest((verticalSize * horizontalSize), true, position);
                break;
            case 3:
                this.stringMap[x][y] = "TREE3";
                this.map[x][y] = new Forest((verticalSize * horizontalSize), true, position);
                break;
            case 4:
                this.stringMap[x][y] = "TREE4";
                this.map[x][y] = new Forest((verticalSize * horizontalSize), false, position);
                break;
            default:
                this.stringMap[x][y] = "TREE1";
                this.map[x][y] = new Forest((verticalSize * horizontalSize), true, position);
                break;
        }
    }

    public void generateCity(int x, int y){
        int rnd = (int)(Math.random() * 3);
        int maxPop = (int)(Math.random() * 601) + 200; 
        int growthRate = (int)(Math.random() * 30) + 20;
        City sharedCity = new City(new int[]{x, y}, growthRate, maxPop);
        switch(rnd){
            case 1:
                for(int i = 0; i < 3; i++){
                    for (int j = 0; j < 3; j++){
                        int nx = x + i;
                        int ny = y + j;
                        if(nx >= this.horizontal || ny >= this.vertical || this.map[nx][ny] != null){
                            int[] pos = {x, y};
                            this.map[x][y] = new Plains(pos);
                            this.stringMap[x][y] = "PLAINS";
                            return;
                        }
                    }
                }
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        if((i == 0 || i == 2) && j != 1){
                            this.stringMap[x+i][y+j] = "CITY";
                            this.map[x+i][y+j] = sharedCity;
                        }
                        else{
                            int[] pos = {x+i, y+j};
                            this.map[x+i][y+j] = new Road(pos, false, false);
                            this.stringMap[x+i][y + j] = "ROAD";
                        }
                    }
                }
                this.cities.add(sharedCity);
                break;
            default:
                for(int i = 0; i < 3; i++){
                    for (int j = 0; j < 3; j++){
                        int nx = x + i;
                        int ny = y + j;
                        if(nx >= this.horizontal || ny >= this.vertical || this.map[nx][ny] != null){
                            int[] pos = {x, y};
                            this.map[x][y] = new Plains(pos);
                            this.stringMap[x][y] = "PLAINS";
                            return;
                        }
                    }
                }
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        if(i == 0 || i == 2){
                            this.stringMap[x+i][y+j] = "CITY";
                            this.map[x+i][y+j] = sharedCity;
                        }
                        else{
                            int[] pos = {x+i, y+j};
                            this.map[x+i][y+j] = new Road(pos, false, false);
                            this.stringMap[x+i][y + j] = "ROAD";
                        }
                    }
                }
                this.cities.add(sharedCity);
                break;
        }
    }

    public void generateFactory(int x, int y) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int nx = x + i;
                int ny = y + j;
                if (nx >= this.horizontal || ny >= this.vertical || this.map[nx][ny] != null) {
                    int[] pos = {x, y};
                    this.map[x][y] = new Plains(pos);
                    this.stringMap[x][y] = "PLAINS";
                    return;
                }
            }
        }

        double resVal = Math.random();
        Material resource;
        int productionRate = (int)(Math.random() * 30) + 15;
        if (resVal < 0.2) resource = new Coal(0);
        else if (resVal < 0.4) resource = new Gold(0);
        else if (resVal < 0.6) resource = new Iron(0);
        else if (resVal < 0.8) resource = new Stone(0);
        else resource = new Wood(0);
        int[] pos = {x, y};
        Factory sharedFactory = new Factory(pos, resource, productionRate);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.map[x + i][y + j] = sharedFactory;
                this.stringMap[x + i][y + j] = "FACTORY";
            }
            this.factories.add(sharedFactory);
        }
    }

    public String[][] getStringMap() {
        return stringMap;
    }

    public int getVertical() {
        return vertical;
    }

    public int getHorizontal() {
        return horizontal;
    }

    public Tile getTileAt(int x, int y) {
        if (x >= 0 && x < this.horizontal && y >= 0 && y < this.vertical) {
            return this.map[x][y];
        }
        return null;
    }

    public void setTile(int x, int y, Tile newTile, String stringType) {
        if (x >= 0 && x < this.horizontal && y >= 0 && y < this.vertical) {
            this.map[x][y] = newTile;
            this.stringMap[x][y] = stringType;
        }
    }

    public ArrayList<Factory> getFactories() {
        return this.factories;
    }

    public ArrayList<City> getCities() {
        return this.cities;
    }
}

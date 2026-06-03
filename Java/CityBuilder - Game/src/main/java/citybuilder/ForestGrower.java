package citybuilder;

import citybuilder.map.tile.Forest;

import java.util.ArrayList;

public class ForestGrower {
    private ArrayList<int[]> forestCoords;
    private Map map;
    private int mult;
    private double min;
    public ForestGrower(Map map){
        forestCoords = new ArrayList<int[]>();
        this.map = map;
        this.mult = 1;
        this.min = 0.00002;
    }
    public void setMult(int n){
        this.mult = n;
    }
    public void addForestCoord(int[] xy){
        this.forestCoords.add(xy);
    }
    public void growForest(){
        for (int i = 0; i < forestCoords.size(); i++){
            i = i % forestCoords.size();
            int[] xy = forestCoords.get(i);
            switch(map.getStringMap()[xy[0]][xy[1]]){
                case "TREE1":
                    if(Math.random() < (min * mult)){
                        map.setStringMapTile(xy[0],xy[1],"TREE2");
                        map.setTile(xy[0],xy[1],new Forest(2, true, xy), "TREE2");
                    }
                    break;
                case "TREE2":
                    if(Math.random() < (min * mult)){
                        map.setStringMapTile(xy[0],xy[1],"TREE3");
                        map.setTile(xy[0],xy[1],new Forest(3, true, xy), "TREE3");
                    }
                    break;
                case "TREE3":
                    if(Math.random() < (min * mult)){
                        map.setStringMapTile(xy[0],xy[1],"TREE4");
                        map.setTile(xy[0],xy[1],new Forest(4, false, xy), "TREE4");
                    }
                    break;
                case "TREE4":
                    if(Math.random() < (min * mult)) {
                        int rnd = (int) (Math.random() * 5);
                        switch(rnd){
                            case 1:
                                if (xy[0] + 1 < this.map.getHorizontal()) {
                                    if (this.map.getStringMap()[xy[0] + 1][xy[1]].equals("PLAINS")) {
                                        map.setStringMapTile(xy[0] + 1, xy[1], "TREE1");
                                        int[] xy2 = {xy[0] + 1, xy[1]};
                                        map.setTile(xy[0] + 1,xy[1],new Forest(1, true, xy2), "TREE1");
                                        this.forestCoords.add(xy2);
                                    }
                                }
                                break;
                            case 2:
                                if (xy[0] - 1 > 0) {
                                    if (this.map.getStringMap()[xy[0] - 1][xy[1]].equals("PLAINS")) {
                                        map.setStringMapTile(xy[0] - 1, xy[1], "TREE1");
                                        int[] xy2 = {xy[0] - 1, xy[1]};
                                        map.setTile(xy[0] - 1,xy[1],new Forest(1, true, xy2), "TREE1");
                                        this.forestCoords.add(xy2);
                                    }
                                }
                                break;
                            case 3:
                                if (xy[1] - 1 > 0) {
                                    if (this.map.getStringMap()[xy[0]][xy[1]- 1].equals("PLAINS")) {
                                        map.setStringMapTile(xy[0], xy[1] - 1, "TREE1");
                                        int[] xy2 = {xy[0], xy[1] - 1};
                                        map.setTile(xy[0],xy[1] - 1 ,new Forest(1, true, xy2), "TREE1");
                                        this.forestCoords.add(xy2);
                                    }
                                }
                                break;
                            default:
                                if (xy[1] + 1 < map.getVertical()) {
                                    if (this.map.getStringMap()[xy[0]][xy[1] + 1].equals("PLAINS")) {
                                        map.setStringMapTile(xy[0], xy[1] + 1, "TREE1");
                                        int[] xy2 = {xy[0], xy[1] + 1};
                                        map.setTile(xy[0],xy[1] + 1,new Forest(1, true, xy2), "TREE1");
                                        this.forestCoords.add(xy2);
                                    }
                                }
                                break;
                        }
                    }
                    break;
            }
        }
    }
}

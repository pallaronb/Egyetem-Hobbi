package citybuilder.map.tile;

import citybuilder.vehicles.*;

public class Road extends Tile {
    private Vehicle[] vehiclesOnRoad = new Vehicle[2]; // két jármű lehet egyszerre az úton, egyik egyik irányba megy a másik a más
    private boolean isIntersection;
    private boolean isPlayerBuilt;

    public Road(int[] position, boolean isIntersection, boolean isPlayerBuilt) {
        super(position, false);
        this.isIntersection = isIntersection;
        this.isPlayerBuilt = isPlayerBuilt;
    }
    public String getName(){
        return "Road";
    }
    public int getPrice(){
        return 50;
    }
    public boolean canEnter(int direction) { return vehiclesOnRoad[direction] == null; }
    public void enterRoad(Vehicle vehicle, int direction) { vehiclesOnRoad[direction] = vehicle; }
    public void leaveRoad(int direction) { vehiclesOnRoad[direction] = null; }
}

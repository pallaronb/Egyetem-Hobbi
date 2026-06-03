package citybuilder.vehicles;

import java.util.ArrayList;

import citybuilder.map.materials.*;

public class Vehicle {
    private final int speed;
    private final int capacity;
    private final Material carries;
    private double x;
    private double y;
    private ArrayList<int[]> route;
    private ArrayList<int[]> detailedPath;
    private int routeIndex = 0;
    private double moveAccumulator = 0.0;

    public Vehicle(int speed, int capacity, Material carries) {
        this.speed = speed;
        this.capacity = capacity;
        this.carries = carries;
        this.route = new ArrayList<>();
        this.detailedPath = new ArrayList<>();
    }

    public void setInitialPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.route.clear();
        this.route.add(new int[]{x, y});
        this.detailedPath.clear();
        this.detailedPath.add(new int[]{x, y});
        this.routeIndex = 0;
        this.moveAccumulator = 0.0;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public ArrayList<int[]> getRoute() { return this.route; }
    public ArrayList<int[]> getDetailedPath() { return this.detailedPath; }
    public int getSpeed() { return this.speed; }
    public int getRouteIndex() { return this.routeIndex; }
    public void setRouteIndex(int routeIndex) { this.routeIndex = routeIndex; }
    public double getMoveAccumulator() { return this.moveAccumulator; }
    public void setMoveAccumulator(double moveAccumulator) { this.moveAccumulator = moveAccumulator; }

    public void setRoute(ArrayList<int[]> initialRoute) {
        this.route = initialRoute;
    }
    public Material getCargo(){
        return this.carries;
    }

    public int getCapacity(){
        return this.capacity;
    }
    public void setDetailedPath(ArrayList<int[]> path) { this.detailedPath = path; }
}

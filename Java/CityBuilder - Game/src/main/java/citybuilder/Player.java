package citybuilder;

import citybuilder.vehicles.Vehicle;

import java.util.ArrayList;

public class Player {
    private int money;
    private ArrayList<Vehicle> vehicles;

    public Player(){
        this.money = 5000;
        this.vehicles = new ArrayList<Vehicle>();
    }

    public int getMoney() {
        return this.money;
    }

    public void setMoney(int i) {
        this.money = i;
    }

    public ArrayList<Vehicle> getVehicles() {
        return this.vehicles;
    }

    public void addVehicle(Vehicle v) {
        this.vehicles.add(v);
    }
}

package citybuilder.vehicles;

import citybuilder.map.materials.Material;

public class Truck extends Vehicle{
    private String name;
    private int price;
    public Truck(int speed, int capacity, Material carries, String name, int price) {
        super(speed, capacity, carries);
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }
}

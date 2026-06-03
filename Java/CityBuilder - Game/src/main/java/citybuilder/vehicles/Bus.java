package citybuilder.vehicles;

import citybuilder.map.materials.Material;

abstract public class Bus extends Vehicle {
    private int price;
    private String name;
    public Bus(int speed, int capacity, Material carries, int price, String name) {
        super(speed, capacity, carries);
        this.price = price;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}

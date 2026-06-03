package citybuilder.vehicles.woodtrucks;

import citybuilder.map.materials.Material;
import citybuilder.vehicles.Truck;

abstract public class WoodTruck extends Truck {
    public WoodTruck(int speed, int capacity, Material carries, String name, int price) {
        super(speed, capacity, carries, name, price);
    }
}

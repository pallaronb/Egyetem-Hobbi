package citybuilder.vehicles.stonetrucks;

import citybuilder.map.materials.Material;
import citybuilder.vehicles.Truck;

abstract public class StoneTruck extends Truck {
    public StoneTruck(int speed, int capacity, Material carries, String name, int price) {
        super(speed, capacity, carries, name, price);
    }
}

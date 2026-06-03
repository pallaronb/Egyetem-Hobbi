package citybuilder.vehicles.goldtrucks;

import citybuilder.map.materials.Material;
import citybuilder.vehicles.Truck;

abstract public class GoldTruck extends Truck {
    public GoldTruck(int speed, int capacity, Material carries, String name, int price) {
        super(speed, capacity, carries, name, price);
    }
}

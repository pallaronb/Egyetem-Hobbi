package citybuilder.vehicles.irontrucks;

import citybuilder.map.materials.Material;
import citybuilder.vehicles.Truck;

abstract public class IronTruck extends Truck {
    public IronTruck(int speed, int capacity, Material carries, String name, int price) {
        super(speed, capacity, carries, name, price);
    }
}

package citybuilder.vehicles.coaltrucks;

import citybuilder.map.materials.Material;
import citybuilder.vehicles.Truck;

abstract public class CoalTruck extends Truck {
    public CoalTruck(int speed, int capacity, Material carries, String name, int price) {
        super(speed, capacity, carries, name, price);
    }
}

package citybuilder.vehicles;

import citybuilder.vehicles.coaltrucks.LargeCoalTruck;
import citybuilder.map.materials.*;
import citybuilder.vehicles.coaltrucks.*;
import citybuilder.vehicles.goldtrucks.*;
import citybuilder.vehicles.irontrucks.*;
import citybuilder.vehicles.stonetrucks.*;
import citybuilder.vehicles.woodtrucks.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class TruckTest {

    @Test
    void testTruckInitialization() {
        Coal coal = new Coal(100);
        SmallCoalTruck truck = new SmallCoalTruck(5, 100, coal, "Test Truck", 200);

        assertEquals("Test Truck", truck.getName());
        assertEquals(200, truck.getPrice());
        assertEquals(5, truck.getSpeed());
        assertNotNull(truck.getCargo());
    }

    @Test
    void testMovementLogic() {
        Coal coal = new Coal(50);
        SmallCoalTruck truck = new SmallCoalTruck(10, 100, coal, "Speedy", 250);
        truck.setInitialPosition(2, 2);
        assertEquals(2, truck.getX());
        assertEquals(2, truck.getY());
        ArrayList<int[]> route = new ArrayList<>();
        route.add(new int[]{2, 2});
        route.add(new int[]{2, 3});
        truck.setRoute(route);

        assertEquals(2, truck.getRoute().size());
        assertEquals(0, truck.getRouteIndex());
    }
    @Test
    void testCoalTrucks() {
        SmallCoalTruck small = new SmallCoalTruck(7, 20, new Coal(0), "Small Coal Truck", 250);
        LargeCoalTruck large = new LargeCoalTruck(10, 50, new Coal(0), "Large Coal Truck", 500);

        assertNotNull(small);
        assertEquals("Small Coal Truck", small.getName());
        assertInstanceOf(Coal.class, small.getCargo(), "Cargo should be Coal");

        assertNotNull(large);
        assertEquals(50, large.getCapacity());
    }

    @Test
    void testGoldTrucks() {
        SmallGoldTruck small = new SmallGoldTruck(7, 20, new Gold(0), "Small Gold Truck", 250);
        LargeGoldTruck large = new LargeGoldTruck(10, 50, new Gold(0), "Large Gold Truck", 500);

        assertNotNull(small);
        assertEquals("Small Gold Truck", small.getName());
        assertInstanceOf(Gold.class, small.getCargo(), "Cargo should be Gold");

        assertNotNull(large);
        assertEquals(500, large.getPrice());
    }

    @Test
    void testIronTrucks() {
        SmallIronTruck small = new SmallIronTruck(7, 20, new Iron(0), "Small Iron Truck", 250);
        LargeIronTruck large = new LargeIronTruck(10, 50, new Iron(0), "Large Iron Truck", 500);

        assertNotNull(small);
        assertEquals("Small Iron Truck", small.getName());
        assertInstanceOf(Iron.class, small.getCargo(), "Cargo should be Iron");

        assertNotNull(large);
        assertEquals(10, large.getSpeed());
    }

    @Test
    void testStoneTrucks() {
        SmallStoneTruck small = new SmallStoneTruck(7, 20, new Stone(0), "Small Stone Truck", 250);
        LargeStoneTruck large = new LargeStoneTruck(10, 50, new Stone(0), "Large Stone Truck", 500);

        assertNotNull(small);
        assertEquals("Small Stone Truck", small.getName());
        assertInstanceOf(Stone.class, small.getCargo(), "Cargo should be Stone");

        assertNotNull(large);
    }

    @Test
    void testWoodTrucks() {
        SmallWoodTruck small = new SmallWoodTruck(7, 20, new Wood(0), "Small Wood Truck", 250);
        LargeWoodTruck large = new LargeWoodTruck(10, 50, new Wood(0), "Large Wood Truck", 500);

        assertNotNull(small);
        assertEquals("Small Wood Truck", small.getName());
        assertInstanceOf(Wood.class, small.getCargo(), "Cargo should be Wood");

        assertNotNull(large);
    }
}
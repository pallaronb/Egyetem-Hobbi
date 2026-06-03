package citybuilder.vehicles;

import citybuilder.map.materials.Passenger;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class BusTest {

    @Test
    void testBusInitialization() {
        Passenger passengers = new Passenger(200);
        SmallBus bus = new SmallBus(7, 200, passengers, "City Shuttle", 250);

        assertEquals("City Shuttle", bus.getName());
        assertEquals(250, bus.getPrice());
        assertEquals(7, bus.getSpeed());
        assertEquals(200, bus.getCapacity());
        assertNotNull(bus.getCargo());
    }

    @Test
    void testLargeBusSpecifics() {
        Passenger passengers = new Passenger(500);
        LargeBus largeBus = new LargeBus(10, 500, passengers, "Mega Bus", 500);

        assertEquals(10, largeBus.getSpeed());
        assertTrue(largeBus.getCargo() instanceof Passenger);
    }

    @Test
    void testDetailedPathHandling() {
        Passenger passengers = new Passenger(10);
        SmallBus bus = new SmallBus(5, 10, passengers, "Short Route", 100);

        ArrayList<int[]> path = new ArrayList<>();
        path.add(new int[]{0, 0});
        path.add(new int[]{0, 1});

        bus.setDetailedPath(path);

        assertEquals(2, bus.getDetailedPath().size());
        assertEquals(0, bus.getRouteIndex());

        bus.setRouteIndex(1);
        assertEquals(1, bus.getRouteIndex());
    }
}
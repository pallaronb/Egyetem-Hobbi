package citybuilder.tile;

import citybuilder.map.tile.Road;
import citybuilder.vehicles.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RoadTest {
    private Road road;

    @BeforeEach
    void setUp() {
        road = new Road(new int[]{0, 0}, false, true);
    }

    @Test
    void testGetPrice() {
        assertEquals(50, road.getPrice(), "Price should be 50");
    }

    @Test
    void testVehicleInteractions() {
        Vehicle mockVehicle = mock(Vehicle.class);

        assertTrue(road.canEnter(0), "Direction 0 should be free initially");
        assertTrue(road.canEnter(1), "Direction 1 should be free initially");

        road.enterRoad(mockVehicle, 0);

        assertFalse(road.canEnter(0), "Direction 0 should be blocked after entering");
        assertTrue(road.canEnter(1), "Direction 1 should still be free");

        road.leaveRoad(0);

        assertTrue(road.canEnter(0), "Direction 0 should be clear after leaving");
    }
}
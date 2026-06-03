package citybuilder.tile;

import citybuilder.map.tile.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CityTest {

    private City city;

    @BeforeEach
    void setUp() {
        city = new City(new int[]{0, 0}, 100, 500);

        city.updatePopulation(100.0, 1);
    }

    @Test
    void testTakePassengers_PartialAmount() {
        assertEquals(500, city.getAvailablePassengers(), "City should start with max population");

        city.takePassengers(200);

        assertEquals(300, city.getAvailablePassengers(), "City should have 300 passengers left after taking 200");
    }

    @Test
    void testTakePassengers_ExactAmount() {
        city.takePassengers(500);

        assertEquals(0, city.getAvailablePassengers(), "City should have 0 passengers left after taking exactly 500");
    }

    @Test
    void testTakePassengers_MoreThanAvailable() {
        city.takePassengers(600);

        assertEquals(0, city.getAvailablePassengers(), "City population should not go below 0 when attempting to over-draw");
    }
}
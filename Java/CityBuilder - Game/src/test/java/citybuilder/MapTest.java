package citybuilder;

import citybuilder.map.tile.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MapTest {
    private Map map;

    @BeforeEach
    void setUp() {
        map = new Map(20, 20);
    }

    @Test
    void testCreateMapInitializesAllTiles() {
        map.createMap();
        String[][] stringMap = map.getStringMap();

        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                assertNotNull(stringMap[x][y], "Tile string representation should not be null");
                assertNotNull(map.getTileAt(x, y), "Tile object should not be null");
            }
        }
    }

    @Test
    void testCreateMapPopulatesTiles() {
        Map map = new Map(20, 20);
        map.createMap();
        boolean hasContent = false;
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                if (map.getStringMap()[x][y] != null) {
                    hasContent = true;
                    break;
                }
            }
        }
        assertTrue(hasContent, "Map should be populated with tile strings");
    }

    @Test
    void testGenerateCityBounds() {
        map.generateCity(19, 19);
        assertNotNull(map.getTileAt(19, 19));
        assertEquals("PLAINS", map.getStringMap()[19][19]);
    }

    @Test
    void testSetTile() {
        int[] pos = {5, 5};
        Tile road = new Road(pos, false, true);
        map.setTile(5, 5, road, "ROAD");

        assertEquals("ROAD", map.getStringMap()[5][5]);
        assertInstanceOf(Road.class, map.getTileAt(5, 5));
    }
    @Test
    void testGenerateFactory_SuccessAndSize() {
        map.generateFactory(10, 10);

        assertEquals("FACTORY", map.getStringMap()[10][10]);
        assertEquals("FACTORY", map.getStringMap()[11][10]);
        assertEquals("FACTORY", map.getStringMap()[10][11]);
        assertEquals("FACTORY", map.getStringMap()[11][11]);

        citybuilder.map.tile.Factory factory = (citybuilder.map.tile.Factory) map.getTileAt(10, 10);
        assertNotNull(factory.getResource());
    }

    @Test
    void generateCityTest(){
        map.generateCity(10,10);
        assertEquals("CITY", map.getStringMap()[10][10]);
        assertEquals("CITY", map.getStringMap()[12][10]);
        assertEquals("CITY", map.getStringMap()[10][12]);
        assertEquals("CITY", map.getStringMap()[12][12]);
    }
}
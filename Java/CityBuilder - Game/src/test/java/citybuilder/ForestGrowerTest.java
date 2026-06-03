package citybuilder;

import citybuilder.map.tile.Forest;
import citybuilder.map.tile.Plains;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ForestGrowerTest {

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testForestGrowth() {
        Map map = new Map(20, 20);
        for(int i = 0; i< map.getVertical(); i++){
            for (int j = 0; j < map.getHorizontal(); j++){
                int[] posi = {i, j};
                map.setTile(i,j,new Plains(posi), "PLAINS");
            }
        }
        int[] pos = {10, 10};
        map.setTile(10, 10, new Forest(4, false, pos), "TREE4");
        map.getFg().addForestCoord(pos);
        map.getFg().setMult(100000);
        int safetyCounter = 0;
        while (map.getStringMap()[11][11].equals("PLAINS") && safetyCounter < 10000) {
            map.getFg().growForest();
            safetyCounter++;
        }
        assertNotEquals("PLAINS", map.getStringMap()[11][11], "Forest never spread to 11,11");
    }

    @Test
    void testForestGrowingOnTile() {
        Map map = new Map(20, 20);
        map.createMap();
        int[] pos = {10, 10};
        map.setTile(10, 10, new Forest(1, true, pos), "TREE1");
        map.getFg().addForestCoord(pos);
        map.getFg().setMult(1000000);
        int safety = 0;
        while (!map.getStringMap()[10][10].equals("TREE4") && safety < 100) {
            map.getFg().growForest();
            safety++;
        }
        assertEquals("TREE4", map.getStringMap()[10][10], "Forest failed to reach stage 4");
    }
}

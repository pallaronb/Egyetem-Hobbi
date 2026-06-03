package citybuilder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void testGameGetters() {
        Player p = new Player();
        Map m = new Map(10, 10);
        TimeHandler th = new TimeHandler();

        Game game = new Game(p, m, th);

        assertEquals(p, game.getPlayer());
        assertEquals(m, game.getMap());
        assertEquals(th, game.getTimeHandler());
    }
}
package citybuilder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void testInitialBalance() {
        Player player = new Player();
        assertEquals(5000, player.getMoney(), "Initial balance should be 5000");
    }
}
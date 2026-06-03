package citybuilder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TimeHandlerTest {
    @Test
    void testSpeedMultiplierLogic() {
        TimeHandler th = new TimeHandler();
        th.setSpeed(2);
        assertEquals(2, th.getSpeedMultiplier());
    }

    @Test
    void testDeltaTimeCalculation() throws InterruptedException {
        TimeHandler th = new TimeHandler();
        Thread.sleep(50);
        double dt = th.getDeltaTime();

        assertTrue(dt > 0, "Delta time should be positive after elapsed time");
    }
}
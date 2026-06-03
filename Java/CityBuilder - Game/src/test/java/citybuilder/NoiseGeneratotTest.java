package citybuilder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NoiseGeneratorTest {
    @Test
    void testDeterministicNoise() {
        long seed = 12345L;
        NoiseGenerator gen1 = new NoiseGenerator(seed);
        double val1 = NoiseGenerator.noise(0.5, 0.5);

        NoiseGenerator gen2 = new NoiseGenerator(seed);
        double val2 = NoiseGenerator.noise(0.5, 0.5);

        assertEquals(val1, val2, "Noise should be the same for the same seed and coordinates");
    }

    @Test
    void testNoiseGradientTransitions() {
        NoiseGenerator gen = new NoiseGenerator(42L);

        double val1 = NoiseGenerator.noise(0.1, 0.1);
        double val2 = NoiseGenerator.noise(0.9, 0.9);

        assertNotEquals(val1, val2);
    }
}
package gogame;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class GoStateTest {

    private GoState goState;

    @BeforeEach
    public void setUp(){
        goState = new GoState(9);
    }

    @ParameterizedTest
    @ArgumentsSource(PointArguementProvider.class)
    public void getNeighborsTest(Point input, Point output){
        assertEquals(goState.getNeighbors(input), output);
    }

    @ParameterizedTest
    @CsvSource({
            "new Point(2, 2)",
            "new Point(3, 3)",
            "new Point(4, 4)",
    })
    public void isLegalMoveTest(Point p) {
        goState.placeStone(p);
        assertFalse("The space is not Empty", goState.isLegalMove(p));

        goState.placeStone(new Point(0, 1));
        goState.placeStone(new Point(1, 0));
        goState.placeStone(new Point(2, 1));
        goState.placeStone(new Point(1, 2));
        Point suicidePoint = new Point(1, 1);
        assertFalse("That move would be suicide",goState.isLegalMove(suicidePoint));

        goState.placeStone(new Point(1, 0));
        goState.placeStone(new Point(0, 1));
        goState.placeStone(new Point(1, 2));

        GoState nextState = new GoState(goState);

        assertFalse("Game States cannot be repeated",nextState.isLegalMove(new Point(1, 1)));
    }

    @Test
    public void checkCaptureTest(){
        goState.placeStone(new Point(1, 1));

        goState.placeStone(new Point(0, 1));
        goState.placeStone(new Point(2, 1));
        goState.placeStone(new Point(1, 0));

        goState.placeStone(new Point(1, 2));

        assertEquals(1, goState.getWhiteCaptured());
        assertTrue(goState.isLegalMove(new Point(1, 1)));
    }

    @Test
    public void getLibertiesTest(){
        goState.placeStone(new Point(1, 1));
        goState.placeStone(new Point(1, 2));

        Set<Point> scanned = new HashSet<>();
        Point[] liberties = goState.getLiberties(Stone.BLACK, new Point(1, 1), scanned);

        Set<Point> expectedLiberties = new HashSet<>(Arrays.asList(
                new Point(0, 1), new Point(2, 1), new Point(1, 0),
                new Point(0, 2), new Point(2, 2), new Point(1, 3)
        ));

        Set<Point> expectedScanned = new HashSet<>(Arrays.asList(
                new Point(1, 1), new Point(1, 2)
        ));

        assertEquals(expectedLiberties, liberties);
        assertEquals(expectedScanned, scanned);
    }

    @Test
    public void makeMoveTest() {
        goState.makeMove(new Point(0, 0));
        assertFalse(goState.makeMove(new Point(0, 0)));
        assertTrue(goState.makeMove(null));
        goState.makeMove(null);
        assertFalse(goState.makeMove(null));
    }

    @Test
    public void testSaveLoadGame() {
        String filename = "test_game.sav";
        goState.placeStone(new Point(3, 3));
        goState.placeStone(new Point(4, 4));
        File file = new File(filename);
        assertDoesNotThrow(() -> goState.saveGame(file));
        assertTrue(file.exists());
        GoState loadedState = GoState.loadGame(file);
        assertNotNull(loadedState);
        assertEquals(goState, loadedState);
        file.delete();
    }
}

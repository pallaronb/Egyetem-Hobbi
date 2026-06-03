package gogame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static gogame.BoardSize.fromString;
import static org.junit.jupiter.api.Assertions.*;


public class BoardSizeTest {

    @ParameterizedTest
    @CsvSource({
            "9x9,NINE",
            "13x13,THIRTEEN",
            "19x19,NINETEEN"
    })
    public void TestFromString(String s, BoardSize expected){
        assertEquals(expected, fromString(s));
    }

    @Test
    public void testFailingFromString(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fromString("NOT A VALID SIZE");
        });

        String expectedMessage = "NOT A VALID ARGUMENT";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}

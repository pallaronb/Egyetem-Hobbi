package parking;

import parking.*;
import parking.facility.*;
import vehicle.*;

import static check.CheckThat.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import check.*;

public class ParkingLotTest{
    @Test
    public void testConstructorWithInvalidValues(){
        assertDoesNotThrow(() -> new ParkingLot(3,3));
        assertThrows(IllegalArgumentException.class, () -> new ParkingLot(0,0));
    }

    @Test
    public void testTextualRepresentation(){
        ParkingLot pl = new ParkingLot(3,3);
        Gate gate = new Gate(pl);

        Car car1 = new Car("car1", Size.SMALL, 0);
        Car car2 = new Car("car2", Size.SMALL, 1);
        Car car3 = new Car("car3", Size.LARGE, 1);
        Car car4 = new Car("car4", Size.SMALL, 2);

        gate.registerCar(car1);
        gate.registerCar(car2);
        gate.registerCar(car3);
        gate.registerCar(car4);

        // s x x
        // s l l
        // s x x

        String expected1 = """
        S X X
        S L L
        S X X
        """;

        assertEquals(expected1, pl.toString());

        gate.deRegisterCar(car1.getTicketId());
        gate.deRegisterCar(car2.getTicketId());

        // s x x
        // x l l
        // x x x

        String expected2 = """
        S X X
        X L L
        X X X
        """;

        assertEquals(expected2, pl.toString());
    }
}
package parking.facility;
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

public class GateTest{
    @Test
    public void testFindAnyAvailableSpaceForCar(){
        ParkingLot pl = new ParkingLot(3, 3);
        Gate gate = new Gate(pl);

        Car car1 = new Car("car1", Size.SMALL, 0);
        Car car2 = new Car("car2", Size.SMALL, 1);
        Car car3 = new Car("car3", Size.LARGE, 2);
        Car car4 = new Car("car4", Size.SMALL, 3);

        assertEquals(pl.getFloorPlan()[0][0], gate.findAnyAvailableSpaceForCar(car1));
        assertEquals(pl.getFloorPlan()[0][0], gate.findAnyAvailableSpaceForCar(car2));
        assertEquals(pl.getFloorPlan()[0][1], gate.findAnyAvailableSpaceForCar(car3));
        assertEquals(pl.getFloorPlan()[0][0], gate.findAnyAvailableSpaceForCar(car4));
        //ennyi lenne?
    }

    @ParameterizedTest
    @CsvSource({
        "car1, SMALL, 0",
        "car2, SMALL, 1",
        "car3, LARGE, 2"
    })
    public void testFindPreferredAvailableSpaceForCar(String plate, Size size, int preferredFloor){
        ParkingLot pl = new ParkingLot(3,3);
        Gate gate = new Gate(pl);

        Car car = new Car(plate, size, preferredFloor);

        Car dummy1 = new Car("dummy1", Size.SMALL, 0);
        Car dummy2 = new Car("dummy2", Size.SMALL, 0);
        Car dummy3 = new Car("dummy3", Size.SMALL, 0);

        gate.registerCar(dummy1);
        gate.registerCar(dummy2);
        gate.registerCar(dummy3);

        Space space = gate.findPreferredAvailableSpaceForCar(car);

        if (car.getSpotOccupation() == Size.LARGE){
            assertEquals(2, space.getFloorNumber());
            assertEquals(1, space.getSpaceNumber());
        }
        else{
            assertEquals(1, space.getFloorNumber());
            assertEquals(0, space.getSpaceNumber());
        }
        
    }

    @ParameterizedTest
    @CsvSource({
        "car1, SMALL, 0",
        "car2, SMALL, 1",
        "car3, LARGE, 2"
    })
    public void testRegisterCar(String plate, Size size, int preferredFloor){
        //"licensePlate-floorNumber-spaceNumber"

        ParkingLot pl = new ParkingLot(3,3);
        Gate gate = new Gate(pl);

        Car car = new Car(plate, size, preferredFloor);

        Space space = gate.findPreferredAvailableSpaceForCar(car);

        assertEquals(true, gate.registerCar(car));

        if (car.getSpotOccupation() == Size.SMALL){
            assertEquals(pl.getFloorPlan()[preferredFloor][0], space);
            //nem tudjuk tesztelni a listát, mert nincs getterje -_-
            assertEquals(space.getOccupyingCarTicketId(), car.getLicensePlate() +"-" + Integer.toString(space.getFloorNumber()) + "-" + Integer.toString(space.getSpaceNumber()));
        }
        else{
            Space largeSpace = pl.getFloorPlan()[space.getFloorNumber()][space.getSpaceNumber() - 1];
            assertEquals(pl.getFloorPlan()[preferredFloor][0], largeSpace);
            assertEquals(space.getOccupyingCarTicketId(), car.getTicketId());
            assertEquals(space.getOccupyingCarTicketId(), car.getLicensePlate() +"-" + Integer.toString(space.getFloorNumber()) + "-" + Integer.toString(space.getSpaceNumber()));
        }

        
    }

    @ParameterizedTest
    @CsvSource({
        "car1, SMALL, 0",
        "car2, SMALL, 1",
        "car3, LARGE, 2",
        "car4, SMALL, 3"
    })
    public void testDeRegisterCar(String plate, Size size, int preferredFloor){
        //"licensePlate-floorNumber-spaceNumber"

        ParkingLot pl = new ParkingLot(3,3);
        Gate gate = new Gate(pl);

        Car car = new Car(plate, size, preferredFloor);

        Space space = gate.findPreferredAvailableSpaceForCar(car);

        assertEquals(true, gate.registerCar(car));

        gate.deRegisterCar(car.getTicketId());

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                assertEquals(false, pl.getFloorPlan()[i][j].isTaken());
            }
        }
    }
}
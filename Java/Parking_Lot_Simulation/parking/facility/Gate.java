package parking.facility;
import parking.*;
import parking.facility.*;
import vehicle.*;
import java.util.*;

public class Gate{
    private final ArrayList<Car> cars;
    private final ParkingLot parkingLot;

    public Gate(ParkingLot parkingLot){
        if (parkingLot == null || parkingLot.getFloorPlan() == null){
            throw new IllegalArgumentException("ParkingLot or its floor plan is null");
        }
        this.cars = new ArrayList<Car>();
        this.parkingLot = parkingLot;
    }

    private Space findTakenSpaceByCar(Car c){
       for (Space[] row : parkingLot.getFloorPlan()){
            for(Space e : row){
                if (e.isTaken() == true) {
                    if (c.getLicensePlate().equals(e.getCarLicensePlate())){
                        return e;
                    }
                }
            }
        }
        return null;
    }

    private Space findAvailableSpaceOnFloor(int floor, Car c){
        if (parkingLot.getFloorPlan().length <= floor){
            return null;
        }
        
        if (c.getSpotOccupation() == Size.SMALL){
            for (Space e : parkingLot.getFloorPlan()[floor]){
                if (e.isTaken() == false){
                    return e;
                }
            }
            return null;
        }

        for (int i = 1; i < parkingLot.getFloorPlan()[floor].length ; i++){
            if (parkingLot.getFloorPlan()[floor][i].isTaken() == false && parkingLot.getFloorPlan()[floor][i-1].isTaken() == false){
                return parkingLot.getFloorPlan()[floor][i];
            }
        }

        return null;    
    }

    public Space findAnyAvailableSpaceForCar(Car c){
        for (Space[] e : parkingLot.getFloorPlan()){
            if (( this.findAvailableSpaceOnFloor(e[0].getFloorNumber(), c)) != null){
                return ( this.findAvailableSpaceOnFloor(e[0].getFloorNumber(), c));
            }
        }
        return null;
    }

    public Space findPreferredAvailableSpaceForCar(Car c){
        int below  = c.getPreferredFloor() - 1;
        int above  = c.getPreferredFloor() + 1;
        int height = 0;

        for (Space[] e : parkingLot.getFloorPlan()){
            height ++;
        }

        if (( this.findAvailableSpaceOnFloor(c.getPreferredFloor(), c)) != null){
            return this.findAvailableSpaceOnFloor(c.getPreferredFloor(), c);
        }

        for (int i = 0; i < height; i++){
            if (below >= 0 && (this.findAvailableSpaceOnFloor(below, c) != null)){
                return this.findAvailableSpaceOnFloor(below, c);
            }
            if (above < height && (this.findAvailableSpaceOnFloor(above, c) != null)){
                return this.findAvailableSpaceOnFloor(above, c);
            }
            below--;
            above++;
        }

        return null;
    }

    public boolean registerCar(Car c){
        
        if (this.findPreferredAvailableSpaceForCar(c) != null){
            Space dummy = this.findPreferredAvailableSpaceForCar(c);

            String tId = c.getLicensePlate() + "-" + Integer.toString(dummy.getFloorNumber()) + "-" + Integer.toString(dummy.getSpaceNumber());
            c.setTicketId(tId);
            //legyen: "rendszám-floor-space"

            if (c.getSpotOccupation() == Size.LARGE){
                this.cars.add(c);
                this.parkingLot.getFloorPlan()[dummy.getFloorNumber()][dummy.getSpaceNumber()].addOccupyingCar(c);
                this.parkingLot.getFloorPlan()[dummy.getFloorNumber()][dummy.getSpaceNumber() - 1].addOccupyingCar(c);
            }
            else{
                this.cars.add(c);
                this.findPreferredAvailableSpaceForCar(c).addOccupyingCar(c);
            }

            return true;
        }

        if (this.findAnyAvailableSpaceForCar(c) != null){
            Space dummy = this.findAnyAvailableSpaceForCar(c);

            String tId = c.getLicensePlate() + "-" + Integer.toString(dummy.getFloorNumber()) + "-" + Integer.toString(dummy.getSpaceNumber());
            c.setTicketId(tId);

            if (c.getSpotOccupation() == Size.SMALL){
                this.cars.add(c);
                this.findAnyAvailableSpaceForCar(c).addOccupyingCar(c);
                return true;
            }

            if (c.getSpotOccupation() == Size.LARGE){
                this.cars.add(c);
                this.parkingLot.getFloorPlan()[dummy.getFloorNumber()][dummy.getSpaceNumber()].addOccupyingCar(c);
                this.parkingLot.getFloorPlan()[dummy.getFloorNumber()][dummy.getSpaceNumber() - 1].addOccupyingCar(c);

                return true;
            }

        }

        return false;
    }

    public void registerCars(Car... cars){
        try{
            for(Car c : cars){
                if (this.findAnyAvailableSpaceForCar(c) != null){
                    Space dummy = this.findAnyAvailableSpaceForCar(c);

                    String tId = c.getLicensePlate() + "-" + Integer.toString(dummy.getFloorNumber()) + "-" + Integer.toString(dummy.getSpaceNumber());
                    c.setTicketId(tId);

                    if (c.getSpotOccupation() == Size.SMALL){
                        this.cars.add(c);
                        this.findAnyAvailableSpaceForCar(c).addOccupyingCar(c);
                    
                    }

                    if (c.getSpotOccupation() == Size.LARGE){
                        this.cars.add(c);
                        this.parkingLot.getFloorPlan()[dummy.getFloorNumber()][dummy.getSpaceNumber()].addOccupyingCar(c);
                        this.parkingLot.getFloorPlan()[dummy.getFloorNumber() - 1][dummy.getSpaceNumber() - 1].addOccupyingCar(c);

                    }

                }
                else{
                    throw new Exception("Couldn't register car");
                }
            }
        }catch(Exception e){
            System.err.println("failed to register a car. Continuing...");
        }


        return;
    }

    public void deRegisterCar(String ticketId){

        for(Car e : cars){

            if (e.getTicketId().equals(ticketId)){
                
                cars.remove(e);
                
                if (e.getSpotOccupation() == Size.SMALL){
                    findTakenSpaceByCar(e).removeOccupyingCar();
                }
                else{
                    findTakenSpaceByCar(e).removeOccupyingCar();
                    findTakenSpaceByCar(e).removeOccupyingCar();
                }

                e.setTicketId(null);

                return;
            }
        }
    }

}
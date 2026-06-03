package parking.facility;
import vehicle.Car;
import vehicle.Size;

public class Space{
    private final int floorNumber;
    private final int spaceNumber;
    private Car occupyingCar;
    
    public Space(int fn, int sn){
        this.floorNumber = fn;
        this.spaceNumber = sn;
    }

    public int getFloorNumber(){
        return this.floorNumber;
    }

    public int getSpaceNumber(){
        return this.spaceNumber;
    }

    public boolean isTaken(){
        return (occupyingCar != null);
    }

    public void addOccupyingCar(Car c){
        this.occupyingCar = c;
    }

    public void removeOccupyingCar(){
        this.occupyingCar = null;
    }

    public String getCarLicensePlate(){
        return occupyingCar.getLicensePlate();
    }

    public Size getOccupyingCarSize(){
        return occupyingCar.getSpotOccupation();
    }

    public String getOccupyingCarTicketId(){
        return occupyingCar.getTicketId();
    }
}
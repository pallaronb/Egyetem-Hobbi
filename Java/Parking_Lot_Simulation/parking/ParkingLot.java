package parking;
import parking.facility.*;
import vehicle.*;

public class ParkingLot{
    private final Space[][] floorPlan;

    public ParkingLot(int floorNumber, int spaceNumber){
        if(floorNumber < 1 || spaceNumber < 1){
            throw new IllegalArgumentException("FloorNumber and SpaceNumber can't be less than 1");
        }
        floorPlan = new Space[floorNumber][spaceNumber];

        for(int i = 0; i < floorNumber; i++){
            for(int j = 0; j < spaceNumber; j++){
                floorPlan[i][j] = new Space(i, j);
            }
        }
    }

    public Space[][] getFloorPlan(){
        return floorPlan;
    }

    @Override
    public String toString(){
        String string = "";

        for (int i = floorPlan.length - 1; 0 <= i; i--){
            for (int j = 0; j < floorPlan[i].length; j++){

                if(floorPlan[i][j].isTaken() == false){
                    string = string + "X";
                }
                else if(floorPlan[i][j].getOccupyingCarSize() == Size.SMALL){
                    string = string + "S";
                }
                else if(floorPlan[i][j].getOccupyingCarSize() == Size.LARGE){
                    string = string + "L";
                }

                if (j < floorPlan[i].length - 1){
                    string = string + " ";
                }
            }
            string = string + "\n";
        }

        return string;
    }

}
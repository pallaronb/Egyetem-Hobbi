package citybuilder.map.tile;

public class SmallBridge extends Bridge{
    public SmallBridge(int[] position, boolean isIntersection, boolean isPlayerBuilt){
        super(position, isIntersection, isPlayerBuilt, 3);
    }
    @Override
    public String getName(){
        return "Small Bridge";
    }
}

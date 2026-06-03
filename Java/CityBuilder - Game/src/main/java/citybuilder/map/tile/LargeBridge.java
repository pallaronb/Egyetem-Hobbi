package citybuilder.map.tile;

public class LargeBridge extends Bridge{
    public LargeBridge(int[] position, boolean isIntersection, boolean isPlayerBuilt){
        super(position, isIntersection, isPlayerBuilt, 7);
    }
    @Override
    public String getName(){
        return "Large Bridge";
    }
}

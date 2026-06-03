package citybuilder.map.tile;

public class MediumBridge extends Bridge{
    public MediumBridge(int[] position, boolean isIntersection, boolean isPlayerBuilt){
        super(position, isIntersection, isPlayerBuilt, 5);
    }
    @Override
    public String getName(){
        return "Medium Bridge";
    }
}

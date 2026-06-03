package citybuilder.map.tile;

public abstract class Bridge extends Road{
    private int length;
    public Bridge(int[] position, boolean isIntersection, boolean isPlayerBuilt, int length){
        super(position, isIntersection, isPlayerBuilt);
        this.length = length;
    }
    public int getLength(){
        return this.length;
    }
}

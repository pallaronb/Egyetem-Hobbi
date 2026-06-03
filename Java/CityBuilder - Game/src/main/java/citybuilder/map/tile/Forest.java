package citybuilder.map.tile;

public class Forest extends Tile{
    private int num;
    private boolean grows;

    public Forest(int num,boolean grows, int[] position) {
        super(position, true);
        this.num = num;
        this.grows = grows;
    }
}

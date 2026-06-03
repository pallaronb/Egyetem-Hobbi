package citybuilder.map.tile;

public abstract class Tile {
    private int[] position = new int[2];
    private boolean isBuildable;

    public Tile(int[] position, boolean isBuildable) {
        this.position[0] = position[0];
        this.position[1] = position[1];
        this.isBuildable = isBuildable;
    }

    public int[] getPosition() { return position; }
    public void setPosition(int[] position) { this.position = position; }
    
    public int getX() { return position[0]; }
    public int getY() { return position[1]; }
    
    public boolean isBuildable() { return isBuildable; }
}
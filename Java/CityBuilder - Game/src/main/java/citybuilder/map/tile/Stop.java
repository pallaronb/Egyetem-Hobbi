package citybuilder.map.tile;

public class Stop extends Tile {
    private Tile connectedFacility;
    public Stop(int[] position, Tile connectedFacility) {
        super(position, false);
        this.connectedFacility = connectedFacility;
    }

    public Tile getConnectedFacility() {
        return connectedFacility;
    }
}

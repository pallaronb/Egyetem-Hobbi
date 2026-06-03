package citybuilder;

public class Game {
    private Player player;
    private Map map;
    private TimeHandler timeHandler;

    public Game(Player player, Map map, TimeHandler timeHandler){
        this.player = player;
        this.map = map;
        this.timeHandler = timeHandler;
    }
    

    public TimeHandler getTimeHandler() {
        return timeHandler;
    }

    public Map getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }
}

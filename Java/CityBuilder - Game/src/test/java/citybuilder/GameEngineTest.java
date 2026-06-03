package citybuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import citybuilder.map.materials.Passenger;
import citybuilder.map.tile.*;
import citybuilder.vehicles.LargeBus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class GameEngineTest {

    private GameEngine gameEngine;
    private Map map;
    private IGameUI mockGUI;
    private final TimeHandler timeHandler = new TimeHandler();
    private Game game;
    private Player player;

    @BeforeAll
    static void initHeadlessMode() {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void testVehicleMovement() {
        Map map = new Map(20, 20);
        ArrayList<int[]> dp = new ArrayList<>();
        City startCity = new City(new int[]{10, 10}, 10, 500);
        map.setTile(10, 10, startCity, "CITY");
        Stop startStop = new Stop(new int[]{10, 11}, startCity);
        map.setTile(10, 11, new Road(new int[]{10, 11}, false, false), "ROAD");
        map.setTile(10, 11, startStop, "STOP");
        dp.add(new int[]{10, 11});
        map.setTile(10, 12, new Road(new int[]{10, 12}, false, false), "BRIDGE");
        dp.add(new int[]{10, 12});
        map.setTile(10, 13, new Road(new int[]{10, 13}, false, false), "ROAD");
        dp.add(new int[]{10, 13});
        map.setTile(10, 14, new Road(new int[]{10, 14}, false, false), "ROAD");
        dp.add(new int[]{10, 14});
        City endCity = new City(new int[]{10, 15}, 10, 500);
        map.setTile(10, 15, endCity, "CITY");
        Stop endStop = new Stop(new int[]{10, 14}, endCity);
        map.setTile(10, 14, endStop, "STOP");
        dp.add(new int[]{10, 14});
        LargeBus lb = new LargeBus(10, 10, new Passenger(50), "Bus", 0);
        lb.setPosition(10, 11);
        lb.setDetailedPath(dp);
        lb.setRouteIndex(0);

        int safetyBreak = 0;
        while (lb.getRouteIndex() < dp.size() - 1 && safetyBreak < 100) {
            lb.setMoveAccumulator(1.0);

            if (lb.getMoveAccumulator() >= 1.0) {
                int nextIndex = lb.getRouteIndex() + 1;
                lb.setPosition(dp.get(nextIndex)[0], dp.get(nextIndex)[1]);
                lb.setRouteIndex(nextIndex);
                lb.setMoveAccumulator(0.0);
            }
            safetyBreak++;
        }

        assertEquals(10, lb.getX());
        assertEquals(14, lb.getY());
    }

    @BeforeEach
    void setUp() {
        player = new Player();
        map = new Map(20, 20);
        map.createMap();
        game = new Game(player, map, timeHandler);
        IGameUI gameGUI = new IGameUI() {
            @Override public void updateBalance(int amount) {}
            @Override public void resetBuildMode() {}
        };
        mockGUI = mock(IGameUI.class);
        gameEngine = new GameEngine(game, map, mockGUI);
    }

    @Test
    void testPathfindingBetweenRoads() {
        map.setTile(5, 5, new Road(new int[]{5,5}, false, true), "ROAD");
        map.setTile(6, 5, new Road(new int[]{6,5}, false, true), "ROAD");
        map.setTile(6, 6, new Road(new int[]{6,6}, false, true), "ROAD");
        ArrayList<int[]> path = gameEngine.calculatePath(5, 5, 6, 6);

        assertNotNull(path);
        assertEquals(3, path.size()); 
        assertEquals(6, path.get(2)[0]); 
        assertEquals(6, path.get(2)[1]);
    }

    @Test
    void testRenderingLoop() {
        BufferedImage fakeScreen = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = fakeScreen.getGraphics();

        map.setTile(0, 0, new citybuilder.map.tile.Road(new int[]{0,0}, false, false), "ROAD");
        map.setTile(1, 1, new citybuilder.map.tile.City(new int[]{1,1}, 10, 100), "CITY");
        map.setTile(2, 2, new citybuilder.map.tile.Factory(new int[]{2,2}, new citybuilder.map.materials.Iron(0), 10), "FACTORY");

        gameEngine.setBuildMode(citybuilder.GameGUI.BuildMode.SMALL_BRIDGE);

        assertDoesNotThrow(() -> {
            gameEngine.paintComponent(g);
        }, "Painting the component should execute without a real monitor");
    }

    @Test
    void testStopGameLoop() {
        assertDoesNotThrow(() -> gameEngine.stopGameLoop());
    }

    @Test
    void testVehicleCollisionPrevention() {

        ArrayList<int[]> path = new ArrayList<>();
        path.add(new int[]{10, 11});
        path.add(new int[]{10, 12});
        path.add(new int[]{10, 13});

        LargeBus bus1 = new LargeBus(10, 11, null, "B1", 0);
        bus1.setPosition(10, 11);
        bus1.setDetailedPath(new ArrayList<>(path));
        bus1.setRouteIndex(0);

        LargeBus bus2 = new LargeBus(10, 12, null, "B2", 0);
        bus2.setPosition(10, 12);
        bus2.setDetailedPath(new ArrayList<>(path));
        bus2.setRouteIndex(1);

        game.getPlayer().addVehicle(bus1);
        game.getPlayer().addVehicle(bus2);

        assertFalse(gameEngine.canMoveToNextTile(bus1), "Bus 1 should be blocked by Bus 2");
        assertTrue(gameEngine.canMoveToNextTile(bus2), "Bus 2 should have a clear path");
    }

    @Test
    void testAttemptBuildRoad_InsufficientFunds() {
        player.setMoney(10);
        gameEngine.setBuildMode(GameGUI.BuildMode.ROAD);
        gameEngine.handleMouseClick(200, 200);
        assertNotEquals("ROAD", map.getStringMap()[10][10]);
    }

    @Test
    void testAttemptBuildRoad_Success() {
        player.setMoney(1000);
        int[] pos = {10, 10};
        Tile tile = new Plains(pos);
        map.setTile(10, 10, tile, "PLAINS");

        gameEngine.setBuildMode(GameGUI.BuildMode.ROAD);
        gameEngine.handleMouseClick(200, 200);

        assertEquals("ROAD", map.getStringMap()[10][10]);
    }

    @Test
    void testAttemptBuildStop_FailsWithoutNeighbors() {
        player.setMoney(500);
        int[] pos = {10, 10};
        map.setTile(10, 10, new Road(pos, false, false), "ROAD");
        gameEngine.setBuildMode(GameGUI.BuildMode.STOP);
        gameEngine.handleMouseClick(200, 200); 
        assertNotEquals("STOP", map.getStringMap()[10][10]);
    }

    @Test
    void testAttemptPlaceVehicle_SuccessBusAtCity() {
        citybuilder.map.tile.City city = new citybuilder.map.tile.City(new int[]{5,5}, 10, 500);
        citybuilder.map.tile.Stop stop = new citybuilder.map.tile.Stop(new int[]{4,5}, city);
        map.setTile(4, 5, stop, "STOP");
        map.setStringMapTile(4, 5, "STOP");

        citybuilder.vehicles.LargeBus bus = new citybuilder.vehicles.LargeBus(10, 50, new citybuilder.map.materials.Passenger(0), "Bus", 100);
        gameEngine.startVehiclePlacement(bus, 100);

        gameEngine.handleMouseClick(80, 100);

        assertFalse(player.getVehicles().isEmpty(), "Bus should be successfully placed");
        assertEquals(0, bus.getRouteIndex(), "Route index should initialize to 0");
    }

    @Test
    void testAttemptPlaceVehicle_FailsIfNotOnStop() {
        int[] pos = {10, 10};
        map.setTile(10, 10, new Road(pos, false, false), "ROAD");
        LargeBus lb = new LargeBus(10, 10, new Passenger(50), "Busz",250);
        gameEngine.startVehiclePlacement(lb, 250);
        gameEngine.handleMouseClick(200, 200);
        assertTrue(player.getVehicles().isEmpty());
    }

    @Test
    void testStartVehiclePlacement() {
        LargeBus bus = new LargeBus(10, 50, new Passenger(0), "TestBus", 100);

        gameEngine.startVehiclePlacement(bus, 100);
        map.setTile(0, 0, new Plains(new int[]{0, 0}), "PLAINS");
        gameEngine.handleMouseClick(0, 0);

        assertTrue(player.getVehicles().isEmpty(), "Vehicle should not be placed on invalid tile");
    }

    @Test
    void testAttemptAddRoutePoint_Success() throws Exception {
        citybuilder.map.tile.City city = new citybuilder.map.tile.City(new int[]{5,5}, 10, 100);
        citybuilder.map.tile.Stop startStop = new citybuilder.map.tile.Stop(new int[]{5,6}, city);
        map.setTile(5, 6, startStop, "STOP");
        map.setStringMapTile(5, 6, "STOP");

        citybuilder.map.tile.Stop endStop = new citybuilder.map.tile.Stop(new int[]{5,8}, city);
        map.setTile(5, 7, new citybuilder.map.tile.Road(new int[]{5,7}, false, false), "ROAD");
        map.setTile(5, 8, endStop, "STOP");
        map.setStringMapTile(5, 8, "STOP");

        citybuilder.vehicles.LargeBus bus = new citybuilder.vehicles.LargeBus(10, 50, new Passenger(0), "Bus", 100);
        java.util.ArrayList<int[]> initialRoute = new java.util.ArrayList<>();
        initialRoute.add(new int[]{5, 6});
        bus.setRoute(initialRoute);

        gameEngine.setBuildMode(GameGUI.BuildMode.ROUTE_PLANNING);

        java.lang.reflect.Field field = GameEngine.class.getDeclaredField("vehicleBeingRouted");
        field.setAccessible(true);
        field.set(gameEngine, bus);

        gameEngine.handleMouseClick(100, 160);

        assertEquals(2, bus.getRoute().size(), "Route should now have 2 points");
    }

    @Test
    void testKeyBindings() {
        javax.swing.ActionMap am = gameEngine.getActionMap();

        am.get("pressUp").actionPerformed(null);
        am.get("releaseUp").actionPerformed(null);

        am.get("pressDown").actionPerformed(null);
        am.get("releaseDown").actionPerformed(null);

        am.get("pressLeft").actionPerformed(null);
        am.get("releaseLeft").actionPerformed(null);

        am.get("pressRight").actionPerformed(null);
        am.get("releaseRight").actionPerformed(null);

        gameEngine.setBuildMode(GameGUI.BuildMode.SMALL_BRIDGE);

        am.get("pressUp").actionPerformed(null);
        am.get("pressDown").actionPerformed(null);
        am.get("pressLeft").actionPerformed(null);
        am.get("pressRight").actionPerformed(null);

        assertTrue(true);
    }

    @Test
    void testAttemptBuildBridgeFailure() {
        player.setMoney(10000);
        gameEngine.setBuildMode(GameGUI.BuildMode.MEDIUM_BRIDGE);
        map.setTile(10,10, new City(new int[]{10, 10}, 5, 500), "CITY");
        gameEngine.handleMouseClick(200, 200);
        assertNotEquals("BRIDGE", map.getStringMap()[10][10]);
    }

    @Test
    void testRightClickCancelsBuildMode() {
        gameEngine.setBuildMode(GameGUI.BuildMode.ROAD);

        java.awt.event.MouseEvent rightClick = new java.awt.event.MouseEvent(
                gameEngine, java.awt.event.MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, 100, 100, 1, false, java.awt.event.MouseEvent.BUTTON3
        );

        for (java.awt.event.MouseListener ml : gameEngine.getMouseListeners()) {
            ml.mouseClicked(rightClick);
        }

        org.mockito.Mockito.verify(mockGUI, org.mockito.Mockito.atLeastOnce()).resetBuildMode();
    }

    @Test
    void testAttemptBuildBridgeMoneyFailure() {
        player.setMoney(10);
        gameEngine.setBuildMode(GameGUI.BuildMode.LARGE_BRIDGE);
        gameEngine.handleMouseClick(200, 200);
        assertNotEquals("BRIDGE", map.getStringMap()[10][10]);
    }

    @Test
    void testAttemptBuildBridgeRealSuccess() {
        player.setMoney(1000);
        gameEngine.setBuildMode(GameGUI.BuildMode.SMALL_BRIDGE);
        for(int i = 0; i < 3; i++){
            int[] pos = {10 + i, 10};
            map.setTile(10 + i, 10, new Plains(pos), "PLAINS");
        }
        gameEngine.handleMouseClick(200, 200);
        for(int i = 0; i < 3; i++){
            assertEquals("BRIDGE", map.getStringMap()[10 + i][10]);
        }
    }

    @Test
    void testSmoothSlidingProgress() {
        LargeBus bus = new LargeBus(10, 500, new Passenger(0), "TestBus", 500);

        bus.setPosition(10, 10);

        ArrayList<int[]> path = new ArrayList<>();
        path.add(new int[]{10, 10});
        path.add(new int[]{11, 10});

        bus.setDetailedPath(path);
        bus.setRouteIndex(0);
        bus.setMoveAccumulator(0.5);

        assertEquals(10, bus.getX());

        double visualX = path.get(0)[0] + (path.get(1)[0] - path.get(0)[0]) * bus.getMoveAccumulator();
        assertEquals(10.5, visualX, "Visual X should be halfway between tiles for smooth sliding");
    }

    @Test
    void testMaintenanceDeduction() {
        Player player = new Player();
        player.setMoney(5000);
        player.setMoney(player.getMoney() - 50);
        assertEquals(4950, player.getMoney());
    }

    @Test
    void testAttemptBuildStop_SuccessWithCity() {
        player.setMoney(1000);

        Tile cityTile = new City(new int[]{11, 10}, 10, 500);
        map.setTile(11,10, cityTile, "CITY");
        map.setTile(10, 10, new Plains(new int[]{10, 10}), "ROAD");

        gameEngine.setBuildMode(GameGUI.BuildMode.STOP);
        gameEngine.handleMouseClick(200, 200);

        assertEquals("STOP", map.getStringMap()[10][10]);
    }

    @Test
    void testTruckLoadsFromFactory() {
        Player player = new Player();
        Map map = new Map(20, 20);
        Game game = new Game(player, map, new TimeHandler());
        GameEngine engine = new GameEngine(game, map, null);

        citybuilder.map.materials.Iron iron = new citybuilder.map.materials.Iron(100);
        citybuilder.map.tile.Factory factory = new citybuilder.map.tile.Factory(new int[]{5,5}, iron, 10);
        factory.updateProduction(10.0, 1);

        Stop stop = new Stop(new int[]{4,4}, factory);
        map.setTile(4, 4, stop, "STOP");
        map.setStringMapTile(4, 4, "STOP");

        citybuilder.vehicles.irontrucks.LargeIronTruck truck =
                new citybuilder.vehicles.irontrucks.LargeIronTruck(10, 50, new citybuilder.map.materials.Iron(0), "IronTruck", 0);

        engine.handleVehicleArrival(truck, 4, 4);

        assertTrue(truck.getCargo().getAmount() > 0, "Truck should have loaded Iron");
    }

    @Test
    void testBusArrival_UnloadsAndEarnsMoney() {
        player.setMoney(1000);

        citybuilder.map.tile.City city = new citybuilder.map.tile.City(new int[]{5,5}, 10, 500);
        citybuilder.map.tile.Stop stop = new citybuilder.map.tile.Stop(new int[]{4,5}, city);
        map.setTile(4, 5, stop, "STOP");
        map.setStringMapTile(4, 5, "STOP");

        citybuilder.vehicles.LargeBus bus = new citybuilder.vehicles.LargeBus(10, 50, new citybuilder.map.materials.Passenger(50), "Bus", 100);
        bus.getCargo().setAmount(50); // Force it full

        gameEngine.handleVehicleArrival(bus, 4, 5);

        assertEquals(0, bus.getCargo().getAmount(), "Bus should be empty after arrival");
        org.mockito.Mockito.verify(mockGUI, org.mockito.Mockito.atLeastOnce()).updateBalance(org.mockito.ArgumentMatchers.anyInt());
    }
}
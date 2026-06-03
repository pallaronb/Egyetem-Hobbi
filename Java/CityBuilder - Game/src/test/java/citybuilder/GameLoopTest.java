package citybuilder;

import citybuilder.map.materials.Iron;
import citybuilder.map.materials.Material;
import citybuilder.map.materials.Passenger;
import citybuilder.map.tile.City;
import citybuilder.map.tile.Factory;
import citybuilder.vehicles.LargeBus;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class GameLoopTest {
    @Test
    void testFactoryProductionOverTime() {
        Material iron = new Iron(0);
        Factory factory = new Factory(new int[]{0,0}, iron, 100);

        factory.updateProduction(1.0, 1);

        assertTrue(factory.getAvailableStorage() > 0, "Factory should produce materials over time");
    }

    @Test
    void testCityPopulationGrowth() {
        City city = new City(new int[]{0,0}, 10, 500);
        int initialPop = city.getAvailablePassengers();

        city.updatePopulation(1.0, 1);

        assertTrue(city.getAvailablePassengers() > initialPop, "City population should grow over time");
    }

    @Test
    void testMaintenanceDeductionLogic() {
        GameGUI mockGUI = mock(GameGUI.class);
        Game mockGame = mock(Game.class);
        Player realPlayer = new Player();
        TimeHandler mockTime = mock(TimeHandler.class);

        when(mockGame.getPlayer()).thenReturn(realPlayer);
        when(mockGame.getTimeHandler()).thenReturn(mockTime);

        when(mockTime.getDeltaTime()).thenReturn(10.0);
        when(mockTime.getSpeedMultiplier()).thenReturn(1);

        Map map = new Map(20, 20);
        GameEngine gameEngine = new GameEngine(mockGame, map, mockGUI);
        GameEngine.GameLoopListener listener = gameEngine.new GameLoopListener();

        realPlayer.addVehicle(new LargeBus(10, 500, new Passenger(0), "B1", 0));
        realPlayer.addVehicle(new LargeBus(10, 500, new Passenger(0), "B2", 0));

        listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        verify(mockGUI, atLeastOnce()).updateBalance(anyInt());
    }
}

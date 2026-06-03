package citybuilder;

import citybuilder.map.materials.*;
import citybuilder.map.tile.LargeBridge;
import citybuilder.map.tile.MediumBridge;
import citybuilder.map.tile.Road;
import citybuilder.map.tile.SmallBridge;
import citybuilder.vehicles.Bus;
import citybuilder.vehicles.LargeBus;
import citybuilder.vehicles.SmallBus;
import citybuilder.vehicles.Truck;
import citybuilder.vehicles.Vehicle;
import citybuilder.vehicles.coaltrucks.LargeCoalTruck;
import citybuilder.vehicles.coaltrucks.SmallCoalTruck;
import citybuilder.vehicles.goldtrucks.LargeGoldTruck;
import citybuilder.vehicles.goldtrucks.SmallGoldTruck;
import citybuilder.vehicles.irontrucks.LargeIronTruck;
import citybuilder.vehicles.irontrucks.SmallIronTruck;
import citybuilder.vehicles.stonetrucks.LargeStoneTruck;
import citybuilder.vehicles.stonetrucks.SmallStoneTruck;
import citybuilder.vehicles.woodtrucks.LargeWoodTruck;
import citybuilder.vehicles.woodtrucks.SmallWoodTruck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GameGUI extends JFrame implements IGameUI {

    private final Game game;
    private final GameEngine gamePanel;

    private JPanel menuContainer;
    private JPanel subMenuPanel;
    private JPanel itemMenuPanel;
    private JLabel balanceLabel;

    public ArrayList<Bus> buses;
    public ArrayList<Road> roads;
    public ArrayList<Truck> trucks;
    public enum BuildMode { NONE, ROAD, STOP, PLACE_VEHICLE, ROUTE_PLANNING, SMALL_BRIDGE, MEDIUM_BRIDGE, LARGE_BRIDGE}
    private BuildMode currentMode = BuildMode.NONE;
    private boolean isGameOver = false;

    public GameGUI(Player player, Map map, TimeHandler timeHandler){
        this.game = new Game(player, map, timeHandler);
        this.gamePanel = new GameEngine(game, map, this);
        this.buses = new ArrayList<>();
        this.trucks = new ArrayList<>();
        this.roads = new ArrayList<>();

        int[] pos = {0,0};
        roads.add(new Road(pos, false, false));
        roads.add(new SmallBridge(pos, false, false));
        roads.add(new MediumBridge(pos, false, false));
        roads.add(new LargeBridge(pos, false, false));

        buses.add(new SmallBus(7, 200, new Passenger(200), "Small Bus", 250));
        buses.add(new LargeBus(10, 500, new Passenger(500), "Large Bus", 500));

        trucks.add(new SmallCoalTruck(7,200,new Coal(200),"Small Coal Truck", 250));
        trucks.add(new SmallGoldTruck(7,200,new Gold(200),"Small Gold Truck", 250));
        trucks.add(new SmallIronTruck(7,200,new Iron(200),"Small Iron Truck", 250));
        trucks.add(new SmallStoneTruck(7,200,new Stone(200),"Small Stone Truck", 250));
        trucks.add(new SmallWoodTruck(7,200,new Wood(200),"Small Wood Truck", 250));

        trucks.add(new LargeCoalTruck(10,500,new Coal(500), "Large Coal Truck", 500));
        trucks.add(new LargeGoldTruck(10,500,new Gold(500),"Large Gold Truck", 500));
        trucks.add(new LargeIronTruck(10,500,new Iron(500),"Large Iron Truck", 500));
        trucks.add(new LargeStoneTruck(10,500,new Stone(500),"Large Stone Truck", 500));
        trucks.add(new LargeWoodTruck(10,500,new Wood(500),"Large Wood Truck", 500));

        setTitle("CloseAAD - City Builder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setupNorthMenu();

        add(gamePanel, BorderLayout.CENTER);

        setupSouthMenu();

        pack();
        setSize(1000,800);
        setLocationRelativeTo(null);
        setVisible(true);

        SwingUtilities.invokeLater(() -> {
           menuContainer.revalidate();
           menuContainer.repaint();
           gamePanel.requestFocusInWindow();
        });
    }

    private void setupNorthMenu(){
        menuContainer = new JPanel(new GridBagLayout());
        menuContainer.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JPanel mainTabs = new JPanel(new GridLayout(1, 3));
        mainTabs.setBackground(Color.WHITE);
        mainTabs.add(createMenuButton("Roads", e -> openRoadSection()));
        mainTabs.add(createMenuButton("Stops", e -> openStopSection()));
        mainTabs.add(createMenuButton("Vehicles", e -> openVehicleSection()));

        gbc.gridy = 0;
        menuContainer.add(mainTabs, gbc);

        subMenuPanel = new JPanel(new GridLayout(1, 2));
        subMenuPanel.setBackground(Color.WHITE);
        subMenuPanel.setVisible(false);
        subMenuPanel.setPreferredSize(new Dimension(200,30));
        gbc.gridy = 1;
        menuContainer.add(subMenuPanel, gbc);

        itemMenuPanel = new JPanel(new GridLayout(2, 5));
        itemMenuPanel.setBackground(Color.WHITE);
        itemMenuPanel.setVisible(false);
        itemMenuPanel.setPreferredSize(new Dimension(200,30));
        gbc.gridy = 2;
        menuContainer.add(itemMenuPanel, gbc);

        add(menuContainer, BorderLayout.NORTH);
    }

    private void setupSouthMenu(){
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        balanceLabel = new JLabel("Balance: " + game.getPlayer().getMoney() + "Ft");
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

        JPanel speedPanel = new JPanel(new GridLayout(1,4));
        speedPanel.add(createMenuButton("0x", e -> setSpeed(0)));
        speedPanel.add(createMenuButton("1x", e -> setSpeed(1)));
        speedPanel.add(createMenuButton("2x", e -> setSpeed(2)));
        speedPanel.add(createMenuButton("4x", e -> setSpeed(4)));

        southPanel.add(balanceLabel, BorderLayout.WEST);
        southPanel.add(speedPanel, BorderLayout.CENTER);

        add(southPanel, BorderLayout.SOUTH);
    }

    private void setSpeed(int n){
        game.getTimeHandler().setSpeed(n);
        System.out.println("Game speed set to: "+ n);
    }


    private JButton createMenuButton(String text, ActionListener action){
        JButton btn = new JButton(text);
        btn.setBackground(Color.LIGHT_GRAY);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if(action != null) btn.addActionListener(action);
        btn.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){ btn.setBackground(Color.GRAY);}
            public void mouseExited(MouseEvent e){btn.setBackground(Color.LIGHT_GRAY);}
        });
        return btn;
    }

    private void showTrucks(){
        itemMenuPanel.removeAll();
        for(Truck t : trucks){
            String name = t.getName();
            int price = t.getPrice();
            itemMenuPanel.add(createMenuButton(name, e -> confirmPurchase(name, price)));
        }
        itemMenuPanel.setVisible(true);
        refreshUI();
    }

    private void confirmPurchase(String vehicleName, int price){
        if (game.getPlayer().getMoney() < price) {
            JOptionPane.showMessageDialog(this,
                    "You dont have enough money " + price + " HUF",
                    "Not enough money", 
                    JOptionPane.WARNING_MESSAGE);
            return; 
        }
        int response = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to purchase " + vehicleName + " for: " + price + " HUF?",
                "Purchase Option", JOptionPane.YES_NO_OPTION);
        if(response == JOptionPane.YES_OPTION){
            this.currentMode = BuildMode.PLACE_VEHICLE;
            Vehicle freshVehicle = createNewVehicleInstance(vehicleName);
            gamePanel.startVehiclePlacement(freshVehicle, price); 
            JOptionPane.showMessageDialog(this, "Click directly on a STOP (connected to the required building) to place the vehicle!", "Place Vehicle", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void resetBuildMode() {
        this.currentMode = BuildMode.NONE;
    }

    public void updateBalance(int n){
        if (isGameOver) return;
        game.getPlayer().setMoney(game.getPlayer().getMoney() - n);
        balanceLabel.setText("Balance: " + game.getPlayer().getMoney() + "Ft");
        refreshUI();
        if (game.getPlayer().getMoney() < 0) {
            isGameOver = true; 
            gamePanel.stopGameLoop(); 
            JOptionPane.showMessageDialog(this, 
                "GAME OVER! You have lost!", 
                "Game Over", 
                JOptionPane.ERROR_MESSAGE);
            this.dispose();
            new MenuGUI();
        }
    }

    private Vehicle createNewVehicleInstance(String name) {
        return switch (name) {
            case "Small Bus" -> new SmallBus(7, 25, new Passenger(20), "Small Bus", 250);
            case "Large Bus" -> new LargeBus(10, 50, new Passenger(50), "Large Bus", 500);
            
            case "Small Coal Truck" -> new SmallCoalTruck(7, 20, new Coal(20), "Small Coal Truck", 250);
            case "Small Gold Truck" -> new SmallGoldTruck(7, 20, new Gold(20), "Small Gold Truck", 250);
            case "Small Iron Truck" -> new SmallIronTruck(7, 20, new Iron(20), "Small Iron Truck", 250);
            case "Small Stone Truck" -> new SmallStoneTruck(7, 20, new Stone(20), "Small Stone Truck", 250);
            case "Small Wood Truck" -> new SmallWoodTruck(7, 20, new Wood(20), "Small Wood Truck", 250);
            
            case "Large Coal Truck" -> new LargeCoalTruck(10, 50, new Coal(50), "Large Coal Truck", 500);
            case "Large Gold Truck" -> new LargeGoldTruck(10, 50, new Gold(50), "Large Gold Truck", 500);
            case "Large Iron Truck" -> new LargeIronTruck(10, 50, new Iron(50), "Large Iron Truck", 500);
            case "Large Stone Truck" -> new LargeStoneTruck(10, 50, new Stone(50), "Large Stone Truck", 500);
            case "Large Wood Truck" -> new LargeWoodTruck(10, 50, new Wood(50), "Large Wood Truck", 500);
            
            default -> null;
        };
    }

    private void showBuses(){
        itemMenuPanel.removeAll();
        for(Bus b : buses){
            String name = b.getName();
            int price = b.getPrice();
            itemMenuPanel.add(createMenuButton(name, e -> confirmPurchase(name, price)));
        }

        itemMenuPanel.setVisible(true);
        refreshUI();
    }

    private void openRoadSection(){
        itemMenuPanel.setVisible(false);
        subMenuPanel.removeAll();
        for (Road r : roads){
            String name = r.getName();
            switch(name){
                case "Road":
                    subMenuPanel.add(createMenuButton(name, e -> {
                        this.currentMode = BuildMode.ROAD;
                        gamePanel.setBuildMode(BuildMode.ROAD);
                    }));
                    break;
                case "Small Bridge":
                    subMenuPanel.add(createMenuButton(name, e -> {
                        this.currentMode = BuildMode.SMALL_BRIDGE;
                        gamePanel.setBuildMode(BuildMode.SMALL_BRIDGE);
                    }));
                    break;
                case "Medium Bridge":
                    subMenuPanel.add(createMenuButton(name, e -> {
                        this.currentMode = BuildMode.MEDIUM_BRIDGE;
                        gamePanel.setBuildMode(BuildMode.MEDIUM_BRIDGE);
                    }));
                    break;
                case "Large Bridge":
                    subMenuPanel.add(createMenuButton(name, e -> {
                        this.currentMode = BuildMode.LARGE_BRIDGE;
                        gamePanel.setBuildMode(BuildMode.LARGE_BRIDGE);
                    }));
                    break;
            }
        }
        subMenuPanel.setVisible(true);
        this.currentMode = BuildMode.NONE;
        gamePanel.setBuildMode(BuildMode.NONE);
        refreshUI();
    }
    private void openStopSection(){
        subMenuPanel.setVisible(false);
        itemMenuPanel.setVisible(false);
        this.currentMode = BuildMode.STOP;
        gamePanel.setBuildMode(BuildMode.STOP);
        refreshUI();
    }
    private void openVehicleSection(){
        subMenuPanel.removeAll();
        subMenuPanel.add(createMenuButton("Trucks", e -> showTrucks()));
        subMenuPanel.add(createMenuButton("Buses", e -> showBuses()));

        subMenuPanel.setVisible(true);
        itemMenuPanel.setVisible(false);
        this.currentMode = BuildMode.NONE;
        gamePanel.setBuildMode(BuildMode.NONE);
        refreshUI();
    }

    private void refreshUI(){
        menuContainer.revalidate();
        menuContainer.repaint();
    }
}
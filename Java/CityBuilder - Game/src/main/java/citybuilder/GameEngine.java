package citybuilder;

import citybuilder.map.tile.*;
import citybuilder.vehicles.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;

public class GameEngine extends JPanel {
    private static final int CELL_SIZE = 20;

    private final Game game;
    private int brdir;
    private final Map map;
    private int cameraX = 0;
    private int cameraY = 0;
    private static final int CAMERA_SPEED = 10;

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;


    private final IGameUI parentGUI;
    private GameGUI.BuildMode currentMode = GameGUI.BuildMode.NONE;
    private Vehicle vehicleBeingRouted = null;
    private Vehicle vehicleToPlace = null;
    private int pendingVehiclePrice = 0;
    private double maintenanceAccumulator = 0.0;
    private double maintenanceTimer = 0.0;
    private Timer gameTimer;
    private static Image loadAndScale(String path) {
        try {
            Image img = ImageIO.read(Objects.requireNonNull(GameEngine.class.getResource(path)));
            return img.getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_FAST);
        } catch (Exception e) {
            System.err.println("Hiba a kép betöltésekor: " + path);
            return null;
        }
    }

    public static Image PLAINS;
    public static Image STOP;
    public static Image CITY;
    public static Image FACTORY;
    public static Image RIVER;
    public static Image ROAD;
    public static Image BUS_IMG;
    public static Image TRUCK_IMG;
    public static Image TREE1;
    public static Image TREE2;
    public static Image TREE3;
    public static Image TREE4;
    public static Image BRIDGE;


    static {
        PLAINS = loadAndScale("/Plains.png");
        STOP = loadAndScale("/Stop.png");
        CITY = loadAndScale("/House1.png");
        FACTORY = loadAndScale("/Factory.png");
        RIVER = loadAndScale("/River.png");
        ROAD = loadAndScale("/Road.png");
        BUS_IMG = loadAndScale("/Bus.png");
        TRUCK_IMG = loadAndScale("/Truck.png");
        TREE1 = loadAndScale("/Tree1.png");
        TREE2 = loadAndScale("/Tree2.png");
        TREE3 = loadAndScale("/Tree3.png");
        TREE4 = loadAndScale("/Tree4.png");
        BRIDGE = loadAndScale("/Bridge.png");
    }


    public GameEngine(Game game, Map map, IGameUI parentGUI) {
        this.setPreferredSize(new Dimension(800, 600));
        this.game = game;
        this.map = map;
        this.parentGUI = parentGUI;
        int FPS = 60;

        this.setBackground(Color.BLACK);
        this.setFocusable(true);

        setupKeyBindings();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) || e.getButton() == MouseEvent.BUTTON3) {
                    cancelBuildMode();
                    return;
                }
                handleMouseClick(e.getX(), e.getY());
            }
        });
        this.gameTimer = new Timer(1000 / FPS, new GameLoopListener());
        this.gameTimer.start();
    }

    private void cancelBuildMode(){
        this.currentMode = GameGUI.BuildMode.NONE;
        this.requestFocusInWindow();
        this.vehicleToPlace = null;
        this.vehicleBeingRouted = null;

        if (parentGUI != null) {
            parentGUI.resetBuildMode();
        }
    }

    private Image createImageFromName(String name) {
        if (name == null) return null;
        return switch (name) {
            case "CITY" -> CITY;
            case "FACTORY" -> FACTORY;
            case "TREE1" -> TREE1;
            case "TREE2" -> TREE2;
            case "TREE3" -> TREE3;
            case "TREE4" -> TREE4;
            case "PLAINS" -> PLAINS;
            case "RIVER" -> RIVER;
            case "ROAD" -> ROAD;
            case "STOP" -> STOP;
            case "BRIDGE" -> BRIDGE;
            default -> null;
        };
    }

    private void drawBridgePreview(Graphics g, int x, int y, int brLength) {
        int[][] vectors = {{1, 0}, {0, -1}, {-1, 0}, {0, 1}};
        int dx = vectors[brdir][0];
        int dy = vectors[brdir][1];
        g.setColor(new Color(0, 255, 255, 100));
        for (int i = 0; i < brLength; i++) {
            int px = ((x + (i * dx)) * CELL_SIZE) - cameraX;
            int py = ((y + (i * dy)) * CELL_SIZE) - cameraY;
            g.fillRect(px, py, CELL_SIZE, CELL_SIZE);
            if (i == 0) {
                g.setColor(new Color(255, 255, 255, 150));
                g.drawRect(px, py, CELL_SIZE, CELL_SIZE);
                g.setColor(new Color(0, 255, 255, 100));
            }
        }
    }

    private void drawGame(Graphics g) {
        if (map == null) return;
        String[][] board = map.getStringMap();

        Rectangle clip = g.getClipBounds();
        if (clip == null) {
            clip = new Rectangle(0, 0, getWidth(), getHeight());
        }

        int startX = Math.max(0, cameraX / CELL_SIZE);
        int startY = Math.max(0, cameraY / CELL_SIZE);
        int endX = Math.min(map.getHorizontal(), (cameraX + getWidth()) / CELL_SIZE + 2);
        int endY = Math.min(map.getVertical(), (cameraY + getHeight()) / CELL_SIZE + 2);

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {

                int drawX = (x * CELL_SIZE) - cameraX;
                int drawY = (y * CELL_SIZE) - cameraY;

                String cellContent = board[x][y];
                Image cellImage = createImageFromName(cellContent);

                if (cellImage != null) {
                    g.drawImage(cellImage, drawX, drawY, this);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(drawX, drawY, CELL_SIZE, CELL_SIZE);
                }

                g.setColor(Color.BLACK);
                g.drawRect(drawX, drawY, CELL_SIZE, CELL_SIZE);
            }
        }

        for (Vehicle v : game.getPlayer().getVehicles()) {
            double visualX = v.getX();
            double visualY = v.getY();

            if (v.getDetailedPath().size() > 1) {
                int currentIndex = v.getRouteIndex();
                int nextIndex = getNextRouteIndex(v);

                if (currentIndex < v.getDetailedPath().size() && nextIndex < v.getDetailedPath().size()) {
                    int[] currentTile = v.getDetailedPath().get(currentIndex);
                    int[] nextTile = v.getDetailedPath().get(nextIndex);

                    visualX = currentTile[0] + (nextTile[0] - currentTile[0]) * v.getMoveAccumulator();
                    visualY = currentTile[1] + (nextTile[1] - currentTile[1]) * v.getMoveAccumulator();
                }
            }

            int drawX = (int)(visualX * CELL_SIZE) - cameraX;
            int drawY = (int)(visualY * CELL_SIZE) - cameraY;

            Image imgToDraw = (v instanceof Bus) ? BUS_IMG : TRUCK_IMG;
            if (imgToDraw != null) {
                g.drawImage(imgToDraw, drawX, drawY, CELL_SIZE, CELL_SIZE, this);
            }
        }
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                String cellContent = board[x][y];
                
                if ("FACTORY".equals(cellContent) && map.getFactories() != null) {
                    Tile tile = map.getTileAt(x, y);
                    if (tile instanceof citybuilder.map.tile.Factory) {
                        citybuilder.map.tile.Factory f = (citybuilder.map.tile.Factory) tile;
                        
                        if (map.getFactories().contains(f) && f.getX() == x && f.getY() == y) {
                            int drawX = (x * CELL_SIZE) - cameraX;
                            int drawY = (y * CELL_SIZE) - cameraY;
                            
                            int current = f.getAvailableStorage();
                            int factoryPixelSize = CELL_SIZE * 2; 

                            String matName = f.getResource().getClass().getSimpleName();
                            
                            String text = matName + ": " + current;
                            g.setFont(new Font("Arial", Font.BOLD, 10)); 
                            FontMetrics fm = g.getFontMetrics();
                            
                            int textX = drawX + (factoryPixelSize - fm.stringWidth(text)) / 2; 
                            int textY;
                            if (drawY < 12) {
                                textY = drawY + 12; 
                            } else {
                                textY = drawY - 2; 
                            }
                            g.setColor(Color.BLACK);
                            g.drawString(text, textX + 1, textY + 1);
                            g.setColor(Color.WHITE);
                            g.drawString(text, textX, textY);
                        }
                    }
                }
                
                if ("CITY".equals(cellContent) && map.getCities() != null) {
                    Tile tile = map.getTileAt(x, y);
                    if (tile instanceof City) {
                        City c = (City) tile;
                        if (map.getCities().contains(c) && c.getX() == x && c.getY() == y) {
                            int drawX = (x * CELL_SIZE) - cameraX;
                            int drawY = (y * CELL_SIZE) - cameraY;
                            
                            int current = c.getAvailablePassengers();
                            int cityPixelSize = CELL_SIZE * 3; 
                            
                            String text = "Passengers: " + current;
                            g.setFont(new Font("Arial", Font.BOLD, 10)); 
                            FontMetrics fm = g.getFontMetrics();
                            
                            int textX = drawX + (cityPixelSize - fm.stringWidth(text)) / 2; 
                            
                            int textY;
                            if (drawY < 12) {
                                textY = drawY + 12; 
                            } else {
                                textY = drawY - 2; 
                            }

                            g.setColor(Color.BLACK);
                            g.drawString(text, textX + 1, textY + 1);
                            g.setColor(Color.WHITE);
                            g.drawString(text, textX, textY);
                        }
                    }
                }
            }
        }
        if (isBridgeMode()) {
            int n = switch (currentMode) {
                case SMALL_BRIDGE -> 3;
                case MEDIUM_BRIDGE -> 5;
                case LARGE_BRIDGE -> 7;
                default -> 0;
            };

            Point mousePos = null;

            if (!java.awt.GraphicsEnvironment.isHeadless()) {
                try {
                    mousePos = getMousePosition();
                } catch (Exception e) {
                }
            }

            if (mousePos != null) {
                int gridX = (mousePos.x + cameraX) / CELL_SIZE;
                int gridY = (mousePos.y + cameraY) / CELL_SIZE;
                if (gridX >= 0 && gridX < map.getHorizontal() && gridY >= 0 && gridY < map.getVertical()) {
                    drawBridgePreview(g, gridX, gridY, n);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
    }


    public void setBuildMode(GameGUI.BuildMode mode) {
        this.currentMode = mode;
    }

    void handleMouseClick(int mouseX, int mouseY) {
        int trueMapX = mouseX + cameraX;
        int trueMapY = mouseY + cameraY;
        int gridX = trueMapX / CELL_SIZE;
        int gridY = trueMapY / CELL_SIZE;

        if (gridX >= 0 && gridX < map.getHorizontal() && gridY >= 0 && gridY < map.getVertical()) {
            switch(currentMode){
                case ROAD:
                    attemptBuildRoad(gridX, gridY);
                    break;
                case STOP:
                    attemptBuildStop(gridX, gridY);
                    break;
                case PLACE_VEHICLE:
                    attemptPlaceVehicle(gridX, gridY);
                    break;
                case ROUTE_PLANNING:
                    attemptAddRoutePoint(gridX, gridY);
                    break;
                case NONE:
                    for (Vehicle v : game.getPlayer().getVehicles()) {
                        if (v.getX() == gridX && v.getY() == gridY) {
                            openRouteWindow(v);
                            break;
                        }
                    }
                    break;
                case SMALL_BRIDGE:
                    attemptBuildBridge(gridX, gridY, brdir, 0, new SmallBridge(new int[]{gridX, gridY},false, true));
                    break;
                case MEDIUM_BRIDGE:
                    attemptBuildBridge(gridX, gridY, brdir, 1, new MediumBridge(new int[]{gridX, gridY},false, true));
                    break;
                case LARGE_BRIDGE:
                    attemptBuildBridge(gridX, gridY, brdir, 2, new LargeBridge(new int[]{gridX, gridY},false, true));
                    break;
                default:
                    break;
            }
        }
    }

    private void openRouteWindow(Vehicle v) {
        String vehicleTypeName = v.getClass().getSimpleName();
        String cargoName = v.getCargo().getClass().getSimpleName();

        JDialog dialog = new JDialog((Frame) parentGUI, "Route Planner - " + vehicleTypeName, false);
        dialog.setSize(300, 400);
        dialog.setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.setBackground(Color.DARK_GRAY);

        JLabel nameLabel = new JLabel("Vehicle: " + vehicleTypeName, SwingConstants.CENTER);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel capLabel = new JLabel("Capacity: " + v.getCapacity() + " " + cargoName, SwingConstants.CENTER);
        capLabel.setForeground(Color.LIGHT_GRAY);

        infoPanel.add(nameLabel);
        infoPanel.add(capLabel);
        dialog.add(infoPanel, BorderLayout.NORTH);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 0; i < v.getRoute().size(); i++) {
            int[] point = v.getRoute().get(i);
            if (i == 0) listModel.addElement("START (A): [" + point[0] + ", " + point[1] + "]");
            else listModel.addElement("Stop " + i + ": [" + point[0] + ", " + point[1] + "]");
        }
        JList<String> routeList = new JList<>(listModel);
        dialog.add(new JScrollPane(routeList), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Add New Point");
        addBtn.addActionListener(e -> {
            this.currentMode = GameGUI.BuildMode.ROUTE_PLANNING;
            this.vehicleBeingRouted = v;
            dialog.dispose();
            showMessage("Click on a STOP to add a new route point!");
        });


        JButton delBtn = new JButton("Delete Selected");
        delBtn.addActionListener(e -> {
            int selectedIndex = routeList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select a point to delete.");
            } else if (selectedIndex == 0) {
                JOptionPane.showMessageDialog(dialog, "You cannot delete the starting point (A)!");
            } else {
                v.getRoute().remove(selectedIndex);
                updateVehicleRoute(v);
                listModel.remove(selectedIndex);
                listModel.clear();
                for (int i = 0; i < v.getRoute().size(); i++) {
                    int[] point = v.getRoute().get(i);
                    if (i == 0) listModel.addElement("START (A): [" + point[0] + ", " + point[1] + "]");
                    else listModel.addElement("Stop " + i + ": [" + point[0] + ", " + point[1] + "]");
                }
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(delBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void attemptBuildBridge(int x, int y, int d, int n, Bridge b) {
        int bridgeCost = b.getLength() * 75;

        if (game.getPlayer().getMoney() <= bridgeCost || map.getTileAt(x, y) == null) return;

        int[] pos = {x, y};
        Tile newBridge = switch(n) {
            case 0 -> new SmallBridge(pos, false, true);
            case 1 -> new MediumBridge(pos, false, true);
            default -> new LargeBridge(pos, false, true);
        };

        if (parentGUI != null) parentGUI.updateBalance(bridgeCost);

        int[][] vectors = {{1, 0}, {0, -1}, {-1, 0}, {0, 1}};
        int dx = vectors[d][0];
        int dy = vectors[d][1];

        for (int i = 0; i < b.getLength(); i++) {
            int targetX = x + (i * dx);
            int targetY = y + (i * dy);

            if(map.getStringMap()[targetX][targetY].equals("CITY") || map.getStringMap()[targetX][targetY].equals("ROAD") || map.getStringMap()[targetX][targetY].equals("FACTORY") || map.getStringMap()[targetX][targetY].equals("BRIDGE")){
                showMessage("You can't build bridges on already built structures");
                return;
            }
        }

        for (int i = 0; i < b.getLength(); i++) {
            int targetX = x + (i * dx);
            int targetY = y + (i * dy);

            map.setTile(targetX, targetY, newBridge, "BRIDGE");

            int screenX = (targetX * CELL_SIZE) - cameraX;
            int screenY = (targetY * CELL_SIZE) - cameraY;
            repaint(screenX, screenY, CELL_SIZE, CELL_SIZE);
        }
    }

    private void attemptBuildRoad(int x, int y) {
        int roadCost = 50;

        if (game.getPlayer().getMoney() >= roadCost) {

            Tile targetTile = map.getTileAt(x, y);
            if (targetTile != null && targetTile.isBuildable()) {
                if(parentGUI != null){
                    parentGUI.updateBalance(roadCost);
                }

                int[] pos = {x, y};
                citybuilder.map.tile.Road newRoad = new citybuilder.map.tile.Road(pos, false, true);
                map.setTile(x, y, newRoad, "ROAD");

                int screenX = (x * CELL_SIZE) - cameraX;
                int screenY = (y * CELL_SIZE) - cameraY;
                repaint(screenX, screenY, CELL_SIZE, CELL_SIZE);

            }
        }
    }

    private void attemptBuildStop(int x, int y) {
        int stopCost = 100;

        if (game.getPlayer().getMoney() < stopCost) {
            return;
        }

        if (!"ROAD".equals(map.getStringMap()[x][y])) {
            showMessage("Stops can only be built on existing Roads!");
            return;
        }

        Tile connectedFacility = null;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];

            if (nx >= 0 && nx < map.getHorizontal() && ny >= 0 && ny < map.getVertical()) {
                String neighborType = map.getStringMap()[nx][ny];
                
                if ("CITY".equals(neighborType) || "FACTORY".equals(neighborType)) {
                    connectedFacility = map.getTileAt(nx, ny);
                    break; 
                }
            }
        }

        if (connectedFacility != null) {
            if (parentGUI != null) {
                parentGUI.updateBalance(stopCost);
            }

            int[] pos = {x, y};
            
            Stop newStop = new Stop(pos, connectedFacility);
            map.setTile(x, y, newStop, "STOP");

            int screenX = (x * CELL_SIZE) - cameraX;
            int screenY = (y * CELL_SIZE) - cameraY;
            repaint(screenX, screenY, CELL_SIZE, CELL_SIZE);
        } else {
            showMessage("A Stop must be adjacent to a City or a Factory!");
        }
    }
    private boolean isBridgeMode() {
        return currentMode == GameGUI.BuildMode.SMALL_BRIDGE ||
                currentMode == GameGUI.BuildMode.MEDIUM_BRIDGE ||
                currentMode == GameGUI.BuildMode.LARGE_BRIDGE;
    }

    private void setupKeyBindings() {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("pressed UP"), "pressUp");
        inputMap.put(KeyStroke.getKeyStroke("released UP"), "releaseUp");
        inputMap.put(KeyStroke.getKeyStroke("pressed DOWN"), "pressDown");
        inputMap.put(KeyStroke.getKeyStroke("released DOWN"), "releaseDown");
        inputMap.put(KeyStroke.getKeyStroke("pressed LEFT"), "pressLeft");
        inputMap.put(KeyStroke.getKeyStroke("released LEFT"), "releaseLeft");
        inputMap.put(KeyStroke.getKeyStroke("pressed RIGHT"), "pressRight");
        inputMap.put(KeyStroke.getKeyStroke("released RIGHT"), "releaseRight");

        actionMap.put("pressUp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(isBridgeMode()){
                    GameEngine.this.brdir = 1;
                }else{
                    upPressed = true;
                }
            }
        });
        actionMap.put("releaseUp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                upPressed = false;
            }
        });
        actionMap.put("pressDown", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(isBridgeMode()){
                    GameEngine.this.brdir = 3;
                }else{
                    downPressed = true;
                }
            }
        });
        actionMap.put("releaseDown", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                downPressed = false;
            }
        });
        actionMap.put("pressLeft", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(isBridgeMode()){
                    GameEngine.this.brdir = 2;
                }else{
                    leftPressed = true;
                }
            }
        });
        actionMap.put("releaseLeft", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                leftPressed = false;
            }
        });
        actionMap.put("pressRight", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(isBridgeMode()){
                    GameEngine.this.brdir = 0;
                }else{
                    rightPressed = true;
                }
            }
        });
        actionMap.put("releaseRight", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                rightPressed = false;
            }
        });
    }

    class GameLoopListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            double dt = game.getTimeHandler().getDeltaTime();
            handleCameraMovement(dt);
            if (map != null) {
                for (Factory f : map.getFactories()) {
                    f.updateProduction(dt, game.getTimeHandler().getSpeedMultiplier());
                }
                for (City c : map.getCities()) {
                    c.updatePopulation(dt, game.getTimeHandler().getSpeedMultiplier());
                }
            }
            if (game != null && game.getTimeHandler().getSpeedMultiplier() > 0) {
                int vehicleCount = game.getPlayer().getVehicles().size();
                if (vehicleCount > 0) {
                    double gameDt = dt * game.getTimeHandler().getSpeedMultiplier();
                    maintenanceTimer += gameDt;
                    double costPerSec = 4.0 * vehicleCount;
                    maintenanceAccumulator += costPerSec * gameDt;
                    if (maintenanceTimer >= 10.0) {
                        int deductAmount = (int) maintenanceAccumulator;
                        if (parentGUI != null) {
                            parentGUI.updateBalance(deductAmount);
                        } else {
                            game.getPlayer().setMoney(game.getPlayer().getMoney() - deductAmount);
                        }
                        maintenanceTimer -= 10.0;
                        maintenanceAccumulator -= deductAmount;
                    }
                }
            }
            assert map != null;
            map.getFg().setMult(game.getTimeHandler().getSpeedMultiplier());
            map.getFg().growForest();
            repaint();
        }
        private void handleCameraMovement(double dt){
            boolean moved = false;

            if (upPressed) {
                cameraY -= CAMERA_SPEED;
                moved = true;
            }
            if (downPressed) {
                cameraY += CAMERA_SPEED;
                moved = true;
            }
            if (leftPressed) {
                cameraX -= CAMERA_SPEED;
                moved = true;
            }
            if (rightPressed) {
                cameraX += CAMERA_SPEED;
                moved = true;
            }

            if (game != null && game.getPlayer() != null) {
                for (Vehicle v : game.getPlayer().getVehicles()) {
                    if (v.getDetailedPath().size() > 1) { 
                        if(game.getTimeHandler().getSpeedMultiplier() > 0){
                            v.setMoveAccumulator(v.getMoveAccumulator() + (v.getSpeed() * dt) * game.getTimeHandler().getSpeedMultiplier());
                        }
                        if (v.getMoveAccumulator() >= 1.0) {
                            
                            if (canMoveToNextTile(v)) {
                                int nextIndex = getNextRouteIndex(v);

                                v.setRouteIndex(nextIndex);
                                int[] nextPos = v.getDetailedPath().get(v.getRouteIndex());
                                v.setPosition(nextPos[0], nextPos[1]);
                                v.setMoveAccumulator(0.0);
                                moved = true;
                                handleVehicleArrival(v, nextPos[0], nextPos[1]);
                            } else {
                                v.setMoveAccumulator(1.0);
                            }
                        }
                    }
                }
            }

            if (moved && map != null) {
                int maxCameraX = Math.max(0, (map.getHorizontal() * CELL_SIZE) - getWidth());
                int maxCameraY = Math.max(0, (map.getVertical() * CELL_SIZE) - getHeight());

                cameraX = Math.max(0, Math.min(cameraX, maxCameraX));
                cameraY = Math.max(0, Math.min(cameraY, maxCameraY));

                repaint();
            }
        }
    }

    public void startVehiclePlacement(citybuilder.vehicles.Vehicle v, int price) {
        this.currentMode = GameGUI.BuildMode.PLACE_VEHICLE;
        this.vehicleToPlace = v;
        this.pendingVehiclePrice = price;
    }

    private void attemptPlaceVehicle(int x, int y) {
        if (!"STOP".equals(map.getStringMap()[x][y])) {
            showMessage("You can only place vehicles on a STOP!");
            return;
        }

        Tile targetTile = map.getTileAt(x, y);
        if (!(targetTile instanceof Stop)) return;
        
        Stop stopTile = (Stop) targetTile;
        Tile connectedFacility = stopTile.getConnectedFacility(); 

        boolean hasRequiredBuilding = false;
        
        if (this.vehicleToPlace instanceof Bus && connectedFacility instanceof City) {
            hasRequiredBuilding = true;
        } else if (this.vehicleToPlace instanceof Truck && connectedFacility instanceof Factory) {
            hasRequiredBuilding = true;
        }
        
        if (hasRequiredBuilding) {
            this.vehicleToPlace.setInitialPosition(x, y);
            this.vehicleToPlace.getCargo().setAmount(0);
            ArrayList<int[]> initialRoute = new ArrayList<>();
            initialRoute.add(new int[]{x, y});
            this.vehicleToPlace.setRoute(initialRoute);
            ArrayList<int[]> initialDP = new ArrayList<>();
            initialDP.add(new int[]{x, y});
            this.vehicleToPlace.setDetailedPath(initialDP);
            this.vehicleToPlace.setRouteIndex(0);
            this.vehicleToPlace.setMoveAccumulator(0.0);
            game.getPlayer().addVehicle(this.vehicleToPlace);
            handleVehicleArrival(this.vehicleToPlace, x, y);
            if (parentGUI != null) {
                parentGUI.updateBalance(pendingVehiclePrice);
                parentGUI.resetBuildMode();
            }
            this.currentMode = GameGUI.BuildMode.NONE;
            this.vehicleToPlace = null;
            
            int screenX = (x * CELL_SIZE) - cameraX;
            int screenY = (y * CELL_SIZE) - cameraY;
            repaint(screenX, screenY, CELL_SIZE, CELL_SIZE);
            
            System.out.println("Vehicle successfully placed!");
            
        } else {
            if (this.vehicleToPlace instanceof Bus) {
             showMessage("Error: The bus must be placed on a Stop connected to a CITY!");
            } else {
             showMessage("Error: The truck must be placed on a Stop connected to a FACTORY!");
            }
        }
    }

    
    private void attemptAddRoutePoint(int x, int y) {
        if (!"STOP".equals(map.getStringMap()[x][y])) {
            showMessage("Route points must be on a STOP!");
            return;
        }

        int[] lastPoint = vehicleBeingRouted.getRoute().get(vehicleBeingRouted.getRoute().size() - 1);
        
        if (lastPoint[0] == x && lastPoint[1] == y) {
            showMessage("This stop is already the last point on the route!");
            return;
        }
        
        ArrayList<int[]> pathSteps = calculatePath(lastPoint[0], lastPoint[1], x, y);
        
        if (pathSteps == null) {
            showMessage("No valid road path exists to this point!");
            return;
        }

        int[] firstPoint = vehicleBeingRouted.getRoute().get(0);
        ArrayList<int[]> returnPath = calculatePath(x, y, firstPoint[0], firstPoint[1]);
        if (returnPath == null) {
            showMessage("No return path exists back to the start! Route must be a closed loop.");
            return;
        }

        vehicleBeingRouted.getRoute().add(new int[]{x, y});
        updateVehicleRoute(vehicleBeingRouted);
        
        this.currentMode = GameGUI.BuildMode.NONE;
        showMessage("New stop added to the route!");
    }

    private void updateVehicleRoute(Vehicle v) {
        ArrayList<int[]> stops = v.getRoute();
        if (stops.isEmpty()) return;

        ArrayList<int[]> newDetailedPath = new ArrayList<>();
        if (stops.size() == 1) {
            newDetailedPath.add(stops.get(0));
        } else {
            for (int i = 0; i < stops.size() - 1; i++) {
                int[] current = stops.get(i);
                int[] next = stops.get(i + 1);
                ArrayList<int[]> segment = calculatePath(current[0], current[1], next[0], next[1]);
                if (segment != null) {
                    if (!newDetailedPath.isEmpty() && !segment.isEmpty()) {
                        segment.remove(0);
                    }
                    newDetailedPath.addAll(segment);
                }
            }
            int[] lastStop = stops.get(stops.size() - 1);
            int[] firstStop = stops.get(0);
            ArrayList<int[]> returnSegment = calculatePath(lastStop[0], lastStop[1], firstStop[0], firstStop[1]);
            if (returnSegment != null) {
                if (!newDetailedPath.isEmpty() && !returnSegment.isEmpty()) {
                    returnSegment.remove(0);
                }
                newDetailedPath.addAll(returnSegment);
            }
        }

        int currX = (int) v.getX();
        int currY = (int) v.getY();
        int newIndex = -1;

        for (int i = 0; i < newDetailedPath.size(); i++) {
            if (newDetailedPath.get(i)[0] == currX && newDetailedPath.get(i)[1] == currY) {
                newIndex = i;
                break;
            }
        }

        if (newIndex != -1) {
            v.setDetailedPath(newDetailedPath);
            v.setRouteIndex(newIndex);
        } else {
            ArrayList<int[]> bridgePath = calculatePath(currX, currY, stops.get(0)[0], stops.get(0)[1]);
            
            if (bridgePath != null) {
                ArrayList<int[]> finalPath = new ArrayList<>();
                finalPath.add(new int[]{currX, currY}); 
                for(int[] p : bridgePath) {
                    if (p[0] == newDetailedPath.get(0)[0] && p[1] == newDetailedPath.get(0)[1]) continue;
                    finalPath.add(p);
                }
                finalPath.addAll(newDetailedPath);
                
                v.setDetailedPath(finalPath);
                v.setRouteIndex(0); 
            } else {
                v.setDetailedPath(newDetailedPath);
                v.setRouteIndex(0);
            }
        }
    }

    ArrayList<int[]> calculatePath(int startX, int startY, int targetX, int targetY) {
        if (startX == targetX && startY == targetY) return new ArrayList<>();

        boolean[][] visited = new boolean[map.getHorizontal()][map.getVertical()];
        int[][][] parent = new int[map.getHorizontal()][map.getVertical()][2];

        java.util.Queue<int[]> queue = new java.util.LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        boolean found = false;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int cx = current[0];
            int cy = current[1];

            if (cx == targetX && cy == targetY) {
                found = true;
                break;
            }

            for (int[] dir : dirs) {
                int nx = cx + dir[0];
                int ny = cy + dir[1];
                if (nx >= 0 && nx < map.getHorizontal() && ny >= 0 && ny < map.getVertical()) {
                    String tileType = map.getStringMap()[nx][ny];
                    boolean isNavigable = "ROAD".equals(tileType) || "STOP".equals(tileType) || "BRIDGE".equals(tileType);
                    
                    if (!visited[nx][ny] && isNavigable) {
                        visited[nx][ny] = true;
                        parent[nx][ny][0] = cx;
                        parent[nx][ny][1] = cy;
                        
                        queue.add(new int[]{nx, ny});
                    }
                }
            }
        }

        if (!found) return null;

        ArrayList<int[]> path = new ArrayList<>();
        int currX = targetX;
        int currY = targetY;
        
        while (currX != startX || currY != startY) {
            path.add(0, new int[]{currX, currY}); 
            int pX = parent[currX][currY][0];
            int pY = parent[currX][currY][1];
            currX = pX;
            currY = pY;
        }
        path.add(0,new int[]{startX, startY});
        return path;
    }

    boolean canMoveToNextTile(Vehicle v) {
        if (v.getDetailedPath().size() < 2) return false;
        int nextIndex = getNextRouteIndex(v);
        if (nextIndex >= v.getDetailedPath().size()) return false;
        int[] nextTile = v.getDetailedPath().get(nextIndex);

        if ("STOP".equals(map.getStringMap()[nextTile[0]][nextTile[1]])) {
            return true;
        }
        int myDirX = (int) (nextTile[0] - v.getX());
        int myDirY = (int) (nextTile[1] - v.getY());

        for (Vehicle other : game.getPlayer().getVehicles()) {
            if (other == v) continue; 
            if (other.getX() == nextTile[0] && other.getY() == nextTile[1]) {
                if (other.getDetailedPath().size() < 2) return false; 
                int oNextIndex = getNextRouteIndex(other);
                
                int[] otherNextTile = other.getDetailedPath().get(oNextIndex);
                int otherDirX = (int) (otherNextTile[0] - other.getX());
                int otherDirY = (int) (otherNextTile[1] - other.getY());

                if (myDirX == -otherDirX && myDirY == -otherDirY) {
                    continue; 
                } else {
                    return false;
                }
            }
        }
        return true; 
    }

    private int getNextRouteIndex(Vehicle v) {
        int nextIndex = v.getRouteIndex() + 1;
        
        if (nextIndex >= v.getDetailedPath().size()) {
            if (v.getRoute().size() <= 1) {
                return nextIndex; 
            }

            int[] startStop = v.getRoute().get(0); 
            for (int i = 0; i < v.getDetailedPath().size() - 1; i++) {
                int[] p = v.getDetailedPath().get(i);
                if (p[0] == startStop[0] && p[1] == startStop[1]) {
                    return i + 1;
                }
            }
            return 1; 
        }
        return nextIndex;
    }

    void handleVehicleArrival(Vehicle v, int x, int y) {
        if ("STOP".equals(map.getStringMap()[x][y])) {
            Tile targetTile = map.getTileAt(x, y);
            if (targetTile instanceof Stop) {
                Stop stop = (Stop) targetTile;
                Tile facility = stop.getConnectedFacility();

                if (facility instanceof Factory && v instanceof Truck) {
                    Factory f = (Factory) facility;
                    if (v.getCargo().getClass().equals(f.getResource().getClass())) {
                        int space = v.getCapacity() - v.getCargo().getAmount();
                        int available = f.getAvailableStorage();
                        int toLoad = Math.min(space, available);
                        
                        if (toLoad > 0) {
                            f.takeFromStorage(toLoad);
                            v.getCargo().setAmount(v.getCargo().getAmount() + toLoad);
                        }
                    }
                } 
                else if (facility instanceof City) {
                    City c = (City) facility;
                    int toUnload = v.getCargo().getAmount();
                    if (toUnload > 0) {
                        int moneyEarned = toUnload * v.getCargo().getUnitValue();
                        v.getCargo().setAmount(0);
                        if (parentGUI != null) {
                            parentGUI.updateBalance(-moneyEarned); 
                        } else {
                            game.getPlayer().setMoney(game.getPlayer().getMoney() + moneyEarned);
                        }
                    }
                    if (v instanceof Bus) {
                        int space = v.getCapacity() - v.getCargo().getAmount();
                        int available = c.getAvailablePassengers();
                        int toLoad = Math.min(space, available);
                        
                        if (toLoad > 0) {
                            c.takePassengers(toLoad);
                            v.getCargo().setAmount(v.getCargo().getAmount() + toLoad);
                        }
                    }
                }
            }
        }
    }

    private void showMessage(String message) {
        if (!java.awt.GraphicsEnvironment.isHeadless()) {
            JOptionPane.showMessageDialog(this, message);
        } else {
            System.out.println("GUI MESSAGE: " + message);
        }
    }

    public void stopGameLoop() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }
}

package citybuilder.map.tile;

import citybuilder.map.materials.*;

public class Factory extends Tile {
    private Material productType;
    private int productionRate;
    private double currentStorage;
    private final int maxStorage = 500;

    public Factory(int[] position, Material productType, int productionRate) {
        super(position, false);
        this.productType = productType;
        this.productionRate = productionRate;
        this.currentStorage = 0;
    }

    public void updateProduction(double deltaTime, int speedMultiplier) {
        if (speedMultiplier == 0) return; 

        if (currentStorage < maxStorage) {
            double producedAmount = (productionRate * 0.1) * deltaTime * speedMultiplier;
            currentStorage += producedAmount;
            if (currentStorage > maxStorage) {
                currentStorage = maxStorage;
            }
        }
    }

    public int getAvailableStorage() {
        return (int) currentStorage;
    }

    public void takeFromStorage(int amount) {
        if (amount <= currentStorage) {
            currentStorage -= amount;
        } else {
            currentStorage = 0;
        }
    }

    public Material getResource() {
        return this.productType;
    }
}
package citybuilder.map.tile;

public class City extends Tile {
    private int growthRate; 
    private double currentPopulation; 
    private int maxPopulation; 
    public City(int[] position, int growthRate, int maxPopulation) {
        super(position, false);
        this.growthRate = growthRate;
        this.maxPopulation = maxPopulation;
        this.currentPopulation = 0;
    }

    public void updatePopulation(double deltaTime, int speedMultiplier) {
        if (speedMultiplier == 0) return;

        if (currentPopulation < maxPopulation) {
            double growth = (growthRate * 0.1) * deltaTime * speedMultiplier;
            currentPopulation += growth;

            if (currentPopulation > maxPopulation) {
                currentPopulation = maxPopulation;
            }
        }
    }

    public int getAvailablePassengers() {
        return (int) currentPopulation;
    }

    public void takePassengers(int amount) {
        if (amount <= currentPopulation) {
            currentPopulation -= amount;
        } else {
            currentPopulation = 0;
        }
    }
}
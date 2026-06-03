package citybuilder;

public class TimeHandler {
    private int speedMultiplier = 1;
    private long lastTime;
    public TimeHandler(){
        this.lastTime = System.nanoTime();
    }
    public void setSpeed(int n){
        this.speedMultiplier = n;
    }

    public int getSpeedMultiplier() {
        return speedMultiplier;
    }

    public double getDeltaTime(){
        long currentTime = System.nanoTime();
        double elapsedSeconds = (currentTime - lastTime) / 1000000000.0;
        lastTime = currentTime;
        return elapsedSeconds * speedMultiplier;
    }
}

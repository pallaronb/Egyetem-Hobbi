package citybuilder.map.materials;

public abstract class Material{
    protected int amount;
    protected int unitValue; 
    
    public Material(int amount, int unitValue) {
        this.amount = amount;
        this.unitValue = unitValue;
    }
    
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    
    public int getUnitValue() { return unitValue; }
}

package byow.Items;

public class Coin implements Item {
    private int value;

    public Coin(int value){
        this.value = value;
    }
    @Override
    public String getName() {
        return "Coins worth " + this.value;
    }

    public int getValue(){
        return value;
    }

    public void add(Coin c){
        this.value += c.getValue();
    }

    public String toString(){
        return getName();
    }
}


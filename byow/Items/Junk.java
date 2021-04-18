package byow.Items;

import java.util.Random;

public class Junk implements Item{
    //ToDo Do this later
    private Random random;

    public Junk(Random random){
        this.random =random;
    }

    @Override
    public String getName() {
        return "A piece of junk.";
    }

    public String toString(){
        return getName();
    }
}

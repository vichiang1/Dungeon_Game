package byow.Core;

import byow.TileEngine.Tileset;
import byow.Util.RandomUtils;

import java.util.Random;

public class Bat extends Enemy {

    public Bat(Location loc, Random random) {
        super(loc, random, Tileset.BAT, RandomUtils.uniform(random,1,6));
    }
    public Bat(Location loc, Random random, int level){
        super(loc,random,Tileset.BAT,level);
    }

    @Override
    public int getAgroRadius(){
      return 6;
    }

}

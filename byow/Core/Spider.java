package byow.Core;

import byow.TileEngine.Tileset;
import byow.Util.RandomUtils;

import java.util.Random;

public class Spider extends Enemy {

    public Spider(Location loc, Random random) {
        super(loc, random, Tileset.SPIDER, RandomUtils.uniform(random,6,11));
    }
    public Spider(Location loc, Random random, int level){
        super(loc,random,Tileset.SPIDER,level);
    }

    @Override
    public int getAgroRadius(){
        return 12;
    }
}

package byow.Core;

import byow.Items.*;
import byow.Util.RandomUtils;

import java.util.*;

public class Loot {
    private Enemy enemy;
    private Random random;

    public Loot(Random rand, Enemy enemy) {
        this.enemy = enemy;
        this.random = rand;

    }


    public Item genLoot(int monsterLevel, int luck) {
        int rng = (int) (RandomUtils.uniform(random, 0, 100) + Math.sqrt(luck));
        if (monsterLevel <= 10) return genLoot1(rng,luck);
        else if (monsterLevel <= 20) return genLoot10(rng,luck);
        else if (monsterLevel <= 30) return genLoot20(rng,luck);
        else if (monsterLevel <= 40) return genLoot30(rng,luck);
        else if (monsterLevel <= 50) return genLoot40(rng,luck);
        else if (monsterLevel <= 60) return genLoot50(rng,luck);
        else return genLoot60(rng,luck);
    }


    //Supplementary methods for loot generation
    private Item genItems(int tier, int luck) {
        int rng = RandomUtils.uniform(random,1,6);
        if (rng == 1) {
            return new Sword(tier, luck,random);
        } else if (rng == 2) {
            return new Shield(tier, luck,random);
        } else if (rng == 3) {
            return new Helmet(tier, luck,random);
        } else if (rng == 4) {
            return new Chestpiece(tier, luck,random);
        } else if (rng == 5) {
            return new Leggings(tier, luck,random);
        } else {
            return new Boots(tier, luck,random);
        }
    }

    public Item coins(int tier, int luck) {
        int t = 1;
        for (int i = 1; i < tier; i++) {
            t *= 10;
        }
        int goldGained = RandomUtils.uniform(random,1, 10) * t;
        return new Coin(goldGained);
    }


    //Loot Tables Based on Level of monster killed
    //TODO rebalance Loot Gen
    public Item genLoot1(int rng, int luck) {
        if (rng <= 40) {
            return new Junk(random);
        } else if (rng <= 70) {
            return coins(1,luck);
        } else if (rng <= 100) {
            return genItems(1,luck);
        } else {
            return coins(2,luck);
        }
    }

    public Item genLoot10(int rng, int luck) {
        if (rng <= 30) {
            return coins(1,luck);
        } else if (rng <= 70) {
            return genItems(1,luck);
        } else if (rng <= 90) {
            return coins(2,luck);
        } else if (rng <= 100) {
            return genItems(2,luck);
        } else {
            return coins(3,luck);
        }
    }

    public Item genLoot20(int rng, int luck) {
        if (rng <= 30) {
            return genItems(1,luck);
        } else if (rng <= 60) {
            return coins(2,luck);
        } else if (rng <= 90) {
            return genItems(2,luck);
        } else if (rng <= 100) {
            return coins(3,luck);
        } else {
            return genItems(3,luck);
        }
    }

    public Item genLoot30(int rng, int luck) {
        if (rng <= 30) {
            return coins(2,luck);
        } else if (rng <= 60) {
            return genItems(2,luck);
        } else if (rng <= 90) {
            return coins(3,luck);
        } else if (rng <= 100) {
            return genItems(3,luck);
        } else {
            return coins(4,luck);
        }
    }

    public Item genLoot40(int rng, int luck) {
        if (rng <= 30) {
            return genItems(2,luck);
        } else if (rng <= 60) {
            return coins(3,luck);
        } else if (rng <= 90) {
            return genItems(3,luck);
        } else if (rng <= 100) {
            return coins(4,luck);
        } else {
            return genItems(4,luck);
        }
    }

    public Item genLoot50(int rng, int luck) {
        if (rng <= 30) {
            return coins(3,luck);
        } else if (rng <= 60) {
            return genItems(3,luck);
        } else if (rng <= 90) {
            return coins(4,luck);
        } else if (rng <= 100) {
            return genItems(4,luck);
        } else{
            return coins(5,luck);
        }
    }

    public Item genLoot60(int rng, int luck) {
        if (rng <= 30) {
            return genItems(3,luck);
        } else if (rng <= 60) {
            return coins(4,luck);
        } else if (rng <= 90) {
            return genItems(4,luck);
        } else if (rng <= 100) {
            return coins(5,luck);
        } else{
            return genItems(5,luck);
        }
    }

    //TODO Expand Loot table past Level 60.


}

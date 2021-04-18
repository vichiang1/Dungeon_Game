package byow.Core;

import byow.Items.Item;
import byow.TileEngine.TETile;
import byow.Util.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract public class Enemy implements Occupant {
    //Instance Variables for normal game function
    private TETile tile;
    private Location loc;
    private Random random;
    private boolean isAlive;
    private int agroRadius;

    private static final String[] AGES = {"Baby", "Young", "Adult", "Elder", "Ancient"};
    private static final String[] STRENGTH = {"Weak ", "", "Strong "};
    //Monster Stats
    private int level;
    private int attack;
    private int maxHealth;
    private int healthRegen;
    private int currentHealth;
    private String strength;
    private String age;
    private double expMult;


    public Enemy(Location loc, Random random, TETile tile, int level) {
        this.loc = loc;
        this.random = random;
        this.isAlive = true;
        this.tile = tile;
        agroRadius = 10;
        this.level = level;
        double imEvil = 1; //A variable that creates an exponential increase in monster strength as they level
        for (int i = 0; i < level; i++) imEvil *= 1.01;
        attack = (int) (RandomUtils.uniform(random, 3 * level, 7 * level) * imEvil);
        maxHealth = (int) (RandomUtils.uniform(random, 12 * level, 28 * level) * imEvil);
        healthRegen = (int) ((RandomUtils.uniform(random, (2.0 * level) / 3.0, (4.0 * level) / 3.0) + .5) * imEvil);
        int overall = (int) Math.round((attack / 5.0 + maxHealth / 20.0 + healthRegen) / level); //Determines an overall assesment of the monsters strength
        this.age = AGES[(level - 1) % 5];
        overall = Math.min(4, Math.max(2, overall));
        expMult = (overall-1)/2.0;
        this.strength = STRENGTH[overall - 2];
        this.currentHealth = maxHealth;
    }


    //Methods to interact with combat

    public void setCurrentHealth(int health) {
        this.currentHealth = health;
    }

    public int getCurrentHealth() {
        return this.currentHealth;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public int getLevel() {
        return level;
    }

    public int getHealthRegen() {
        return healthRegen;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        //Enemies usually dont have defense.
        return 0;
    }

    public int getCritChance() {
        //Enemies usually can't crit
        return 0;
    }

    public double getCritDamage() {
        return 1.0;
    }

    public Random getRandom() {
        return random;
    }

    public void passTime() {
        move();
        regen();
    }


    //Methods to interact with the 2D world
    @Override
    public TETile getDisplay() {
        return tile;
    }

    public String getName() {
        return this.strength + this.age + " " + getDisplay().description();
    }

    @Override
    public void die() {
        this.loc.setOccupant(null);
        this.isAlive = false;
    }

    public int getExp(int playerLevel){
        //ToDo: Maybe make luck play a role here.
      return (int) ((RandomUtils.uniform(random,3,8) * level * level * expMult) / playerLevel);
    }

    @Override
    public List<String> getDescription() {
        List<String> description = new ArrayList<>();
        description.add("A fearsome level " + this.level + " " + getName() + ".");
        description.add("Health: " + currentHealth + " / " + maxHealth);
        return description;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    public void setLocation(Location newLocation) {
        //ToDo Decide whether to do a check before doing this (prob should).
        if (newLocation.isPassable()) {
            if (!newLocation.isOccupied()) {
                loc.setOccupant(null);
                loc = newLocation;
                loc.setOccupant(this);
            } else if (newLocation.getOccupant().isPlayer()) {
                new Combat((Player) newLocation.getOccupant(), this);
                if(isAlive()){
                    loc.setOccupant(null);
                    loc = newLocation;
                    loc.setOccupant(this);
                }
            }

        }
    }

    public void move() {
        if (loc.distanceToPlayer() < getAgroRadius()) {
            setLocation(loc.nextStepTowardPlayer());
        } else {
            ArraySet<Location> locs = loc.getUnoccupiedNeighbors(true);
            if (locs.size() > 0) {
                setLocation(locs.getRandom(random));
            }
        }
    }


    public boolean isAlive() {
        return isAlive;
    }

    public int getAgroRadius() {
        return agroRadius;
    }

    public Item getLoot(int luck){
        Loot l = new Loot(random,this);
        return l.genLoot(getLevel(),luck);
    }
}

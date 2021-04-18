package byow.Items;


import byow.Util.RandomUtils;

import java.util.Random;

public class Chestpiece implements Item{

    private String name;
    private int health =0;
    private int reflectDamage = 0;
    private int luck =0;
    private int defense =0;
    private Random random;



    public Chestpiece(Random rand) {
        random = rand;
        name = "Dirty Rags";
    }






    public Chestpiece(int tier, int playerLuck, Random random) {
        this.random = random;
        String[] materials = {"Copper", "Bronze", "Iron", "Steel", "Dark Steel", "Cobalt"};
        //Sets the Multiplier for the Stats
        int mult = 1;
        for(int i =1; i<tier; i++ ){
            mult *= 2;
        }
        //Sets the Defense Stat
        int i=0;
        defense = RandomUtils.uniform(random,1,10);
        while(defense > 9 + 10*i){
            i++;
            defense =RandomUtils.uniform(random,1,10) + 10*i;
        }
        if(defense > 30) defense = 30; //Caps the max defense possible
        defense = defense/3 * mult;
        if(defense < mult) defense = mult; // Sets the min defense possible


        //Sets the Health Stat
        if((int)(random.nextDouble()+ .4 + .1*tier + playerLuck/100.0) > 0){
            health =RandomUtils.uniform(random,1,10);
            i=0;
            while(health > 9 + 10*i){
                i++;
                health =RandomUtils.uniform(random,1,10) + 10*i;
            }
            if(health > 30) health = 30; //Caps health
            health = health * mult;
        }
        //Sets the Reflect Damage Stat
        if((int)(random.nextDouble()+ .4 + .1*tier + playerLuck/100.0) > 0){
            reflectDamage =RandomUtils.uniform(random,1,10);
            i=0;
            while(reflectDamage > 9 + 10*i){
                i++;
                reflectDamage =RandomUtils.uniform(random,1,10) + 10*i;
            }
            if(reflectDamage > 30) reflectDamage = 30; //Caps reflect Damage
            reflectDamage = reflectDamage/3 * mult;
        }


        //Sets the Chestpiece's Luck
        if((int)(random.nextDouble()+ .4 + .1*tier + playerLuck/100.0) > 0){
            luck = RandomUtils.uniform(random,1,10);
            i=0;
            while(luck > 9 + 10*i){
                i++;
                luck = RandomUtils.uniform(random,1,10) + 10*i;
            }
            if(luck > 30) luck= 30; //Caps the Luck
            luck = luck/3 * mult;
        }

        //Sets the Chestpiece's Name
        if(defense*3 > health && defense*3 > reflectDamage && defense*3 > luck*3){
            name = "Dense " + materials[tier-1] + " Chestpiece";
        } else if(health > defense*3 && health > reflectDamage && health > luck*3){
            name = "Hearty " + materials[tier-1] + " Chestpiece";
        } else if(reflectDamage > defense*3 && reflectDamage > health && reflectDamage > luck*3){
            name = "Spiky " + materials[tier-1] + " Chestpiece";
        } else if(luck*3 > defense*3 && luck*3 > reflectDamage && luck*3 > health){
            name = "Lucky " + materials[tier-1] + " Chestpiece";
        } else name= "Balanced " + materials[tier-1] + " Chestpiece";

    }


    public String toString() {
        return name + ": + " + defense + " defense, + " + health + " health, + " + reflectDamage + "% reflect damage, and + " + luck + " luck.";
    }

    //Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public int getReflectDamage() {
        return reflectDamage;
    }
    public void setReflectDamage(int reflectDamage) {
        this.reflectDamage = reflectDamage;
    }
    public int getLuck() {
        return luck;
    }
    public void setLuck(int luck) {
        this.luck = luck;
    }
    public int getDefense() {
        return defense;
    }
    public void setDefense(int defense) {
        this.defense = defense;
    }
}




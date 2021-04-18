package byow.Items;


import byow.Util.RandomUtils;

import java.util.Random;

public class Shield implements Item{
    private String name;
    private int parryChance =0;
    private int parryDamage = 0;
    private int luck =0;
    private int defense =0;
    private Random random;

    public Shield(Random rand) {
        name = "Slab of Bark";
        random = rand;
    }


    public Shield(int tier, int playerLuck, Random random) {
        String[] materials = {"Copper", "Bronze", "Iron", "Steel", "Dark Steel", "Cobalt"};
        this.random = random;
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
        defense *= mult;
        if(defense < 2*mult) defense = 2*mult; // Sets the min defense possible


        //Sets the Parry Chance Stat
        if((int)(random.nextDouble()+ .4 + .1*tier + playerLuck/100.0) > 0){
            parryChance =RandomUtils.uniform(random,1,10);
            i=0;
            while(parryChance > 9 + 10*i){
                i++;
                parryChance =RandomUtils.uniform(random,1,10) + 10*i;
            }
            if(parryChance > 30) parryChance = 30; //Caps Parry Chance
            parryChance = parryChance/2 * mult;
        }
        //Sets the Parry Damage Stat
        if((int)(random.nextDouble()+ .4 + .1*tier + playerLuck/100.0) > 0){
            parryDamage =RandomUtils.uniform(random,1,10);
            i=0;
            while(parryDamage > 9 + 10*i){
                i++;
                parryDamage =RandomUtils.uniform(random,1,10) + 10*i;
            }
            if(parryDamage > 30) parryDamage = 30; //Caps Parry Damage
            parryDamage = parryDamage * mult;
        }


        //Sets the Shields Luck
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
        //Sets the Shields name
        int defenseValue = defense;
        int chanceValue = parryChance *2;
        int damageValue = parryDamage;
        int luckValue = luck *3;
        if(defenseValue > chanceValue && defenseValue > damageValue && defenseValue > luckValue){
            name = "Stalwart " + materials[tier-1] + " Shield";
        } else if(chanceValue > defenseValue && chanceValue > damageValue && chanceValue > luckValue){
            name = "Wide " + materials[tier-1] + " Shield";
        } else if(damageValue > defenseValue && damageValue > chanceValue && damageValue > luckValue){
            name = "Spikey " + materials[tier-1] + " Shield";
        } else if(luckValue > defenseValue && luckValue > damageValue && luckValue > chanceValue){
            name = "Lucky " + materials[tier-1] + " Shield";
        } else name = "Balanced " + materials[tier-1] + " Shield";


    }






    public String toString() {
        return name + ": +" + defense + " defense, + " + parryChance + "% parry chance, + " + parryDamage + "% parry damage, and + " + luck + " luck.";
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParryChance() {
        return parryChance;
    }

    public void setParryChance(int parryChance) {
        this.parryChance = parryChance;
    }

    public int getParryDamage() {
        return parryDamage;
    }

    public void setParryDamage(int parryDamage) {
        this.parryDamage = parryDamage;
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

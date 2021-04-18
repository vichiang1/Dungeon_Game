package byow.Items;


import byow.Util.RandomUtils;

import java.util.Random;

public class Sword implements Item{
    private String name;
    private int critChance =0;
    private int critDamage = 0;
    private int luck =0;
    private int attack =0;
    private Random random;

    public Sword(Random rand) {
        this.random = rand;
        name = "Broken Twig";
    }

    public Sword(int tier, int playerLuck, Random random) {
        this.random = random;
        String[] materials = {"Copper", "Bronze", "Iron", "Steel", "Dark Steel", "Cobalt"};
        //Sets the Multiplier for the Stats
        int mult = (int) Math.pow(2, tier-1);
        //Sets the Attack Stat
        int i=0;
        attack = RandomUtils.uniform(random,1,10);
        while(attack > 9 + 10*i){
            i++;
            attack =RandomUtils.uniform(random,1,10) + 10*i;
        }
        if(attack > 30) attack = 30; //Caps the max attack possible
        attack *= mult;
        if(attack < 2*mult) attack = 2*mult; // Sets the min attack possible


        //Sets the Critical Chance and Damage Stat
        if((int)(random.nextDouble()+ .4 + .1*tier + playerLuck/100.0) > 0){
            critChance =RandomUtils.uniform(random,1,10);
            i=0;
            while(critChance > 9 + 10*i){
                i++;
                critChance =RandomUtils.uniform(random,1,10) + 10*i;
            }
            if(critChance > 30) critChance = 30; //Caps Crit Chance
            critChance = critChance/2 * mult;

            critDamage =RandomUtils.uniform(random,1,10);
            i=0;
            while(critDamage > 9 + 10*i){
                i++;
                critDamage =RandomUtils.uniform(random,1,10) + 10*i;
            }
            if(critDamage > 30) critDamage = 30; //Caps Crit Damage
            critDamage = critDamage/3 * mult;
        }
        if(critDamage<1) critDamage = 1; // Sets the min critDamage possible


        //Sets the Swords Luck
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

        //Sets the Swords Name
        int attackValue =attack;
        int chanceValue = critChance *2;
        int damageValue = critDamage *3;
        int luckValue = luck *3;
        if(attackValue > chanceValue && attackValue > damageValue && attackValue > luckValue){
            name = "Barbaric " + materials[tier-1] + " Sword";
        } else if(chanceValue > attackValue && chanceValue > damageValue && chanceValue > luckValue){
            name = "Precise " + materials[tier -1] + " Sword";
        } else if(damageValue > attackValue && damageValue > chanceValue && damageValue > luckValue){
            name = "Piercing " + materials[tier -1] + " Sword";
        } else if(luckValue > attackValue && luckValue > damageValue && luckValue > chanceValue){
            name = "Lucky "+ materials[tier -1] + " Sword";
        } else {
            name = "Balanced " + materials[i] + " Sword";    	}
    }



    public String toString() {
        return name + ": + " + attack + " attack, + " + critChance + "% crit chance, + " + critDamage + "x crit damage, and + " + luck + " luck.";
    }

    //Getter and Setters
    public int getAttack() {
        return attack;
    }
    public void setAttack(int attack) {
        this.attack = attack;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCritChance() {
        return critChance;
    }

    public void setCritChance(int critChance) {
        this.critChance = critChance;
    }

    public int getCritDamage() {
        return critDamage;
    }

    public void setCritDamage(int critDamage) {
        this.critDamage = critDamage;
    }

    public int getLuck() {
        return luck;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }



}

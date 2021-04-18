package byow.Core;

import byow.Items.*;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player implements Occupant {
    //Instance variables to interact with the 2D world
    private TETile tile;
    private Location loc;
    private Random random;
    private boolean isAlive;

    //Instance variables to interact with combat
    private int currentHealth;
    private int level;
    private int attackStat;
    private int healthStat;
    private int healthRegenStat;
    private int pointsSpent;
    private int totalPoints;
    private int exp;
    private int expLevelUp;

    //Instance Variables regarding Items
    private Coin money;
    private Helmet helmet;
    private Chestpiece chestpiece;
    private Leggings leggings;
    private Boots boots;
    private Sword sword;
    private Shield shield;


    public Player(Location loc, Random random) {
        tile = Tileset.AVATAR;
        this.loc = loc;
        this.random = random;
        loc.setOccupant(this);
        isAlive = true;
        this.expLevelUp = 10;
        this.level = 1;
        this.attackStat = 5;
        this.healthStat = 20;
        this.currentHealth = healthStat;
        this.healthRegenStat = 1;
        this.pointsSpent = 3;
        this.totalPoints = 3;
        this.money = new Coin(0);
        this.helmet = new Helmet(random);
        this.chestpiece = new Chestpiece(random);
        this.leggings = new Leggings(random);
        this.boots = new Boots(random);
        this.sword = new Sword(random);
        this.shield = new Shield(random);
    }

    //Methods to interact with leveling and combat
    public int getLevel() {
        return level;
    }

    public int getMoney() {
        return money.getValue();
    }


    public int getMaxHealth() {
        return healthStat + helmet.getHealth() + chestpiece.getHealth() + leggings.getHealth() + boots.getHealth();
    }

    public int getCurrentHealth() {
        return this.currentHealth;
    }

    public void setCurrentHealth(int h) {
        this.currentHealth = h;
    }

    public int getAttack() {
        return attackStat + sword.getAttack();
    }

    public int getHealthRegen() {
        return healthRegenStat;
    }

    public String getName() {
        return "Player";
    }

    public Random getRandom() {
        return random;
    }

    public int heal() {
        int heal = (int) ((random.nextDouble() + .5) * 5 * this.getHealthRegen());
        setCurrentHealth(Math.min(getCurrentHealth() + heal, getMaxHealth()));
        return heal;
    }

    public int reflect(Occupant o, int damage) {
        int reflected = (int) (damage * getReflectDamage() / 100.0);
        o.setCurrentHealth(o.getCurrentHealth() - reflected);
        return reflected;
    }


    public int getCritChance() {
        return sword.getCritChance();
    }

    public int getParryChance() {
        return 50 + shield.getParryChance();
    }

    public int getLuck() {
        return sword.getLuck() + shield.getLuck() + helmet.getLuck() + chestpiece.getLuck() + leggings.getLuck() + boots.getLuck();
    }

    public int getParryDamage() {
        return 50 + shield.getParryDamage();
    }

    public int getDefense() {
        return shield.getDefense() + helmet.getDefense() + chestpiece.getDefense() + leggings.getDefense() + boots.getDefense();
    }

    public double getCritDamage() {
        return 1 + sword.getCritDamage();
    }

    public int getReflectDamage() {
        return helmet.getReflectDamage() + chestpiece.getReflectDamage() + leggings.getReflectDamage() + boots.getReflectDamage();
    }


    public int getPointsRemaining() {
        return totalPoints - pointsSpent;
    }


    //Item Getter and Setter Methods


    public Helmet getHelmet() {
        return helmet;
    }

    public void setHelmet(Helmet helmet) {
        this.helmet = helmet;
    }

    public Chestpiece getChestpiece() {
        return chestpiece;
    }

    public void setChestpiece(Chestpiece chestpiece) {
        this.chestpiece = chestpiece;
    }

    public Leggings getLeggings() {
        return leggings;
    }

    public void setLeggings(Leggings leggings) {
        this.leggings = leggings;
    }

    public Boots getBoots() {
        return boots;
    }

    public void setBoots(Boots boots) {
        this.boots = boots;
    }

    public Sword getSword() {
        return sword;
    }

    public void setSword(Sword sword) {
        this.sword = sword;
    }

    public Shield getShield() {
        return shield;
    }

    public void setShield(Shield shield) {
        this.shield = shield;
    }

    public int parry(Occupant enemy) {
        boolean success = (random.nextDouble() + (this.getParryChance() - 2 * (enemy.getLevel() - this.getLevel())) / 100.0) > 1;
        if (success) {
            int curr = getCurrentHealth();
            int damage = enemy.attack(this);
            setCurrentHealth(curr);
            enemy.setCurrentHealth(enemy.getCurrentHealth() - (int) (damage * getParryDamage() / 100.0));
            return (int) (damage * getParryDamage() / 100.0);
        }
        return -1;
    }


    public void gainExp(int expGained) {
        this.exp += expGained;
        while (exp >= expLevelUp) {
            levelUp();
        }
    }


    public void gainItem(Item item) {
        String present = "You currently have a ";
        boolean decision = true;
        //ToDo make this generalizable
        if (item instanceof Coin) {
            this.money.add((Coin) item);
            present = "";
            decision = false;
        } else if (item instanceof Junk) {
            present = "";
            decision = false;
        } else if (item instanceof Helmet) {
            present += this.helmet;
        } else if (item instanceof Chestpiece) {
            present += this.chestpiece;
        } else if (item instanceof Leggings) {
            present += this.leggings;
        } else if (item instanceof Boots) {
            present += this.boots;
        } else if (item instanceof Sword) {
            present += this.sword;
        } else {
            present += this.shield;
        }
        if (Engine.display) {
            StdDraw.clear(new Color(0, 0, 0));
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
            int xCenter = Engine.WIDTH / 2;
            double yTop = Engine.HEIGHT / 1.5;
            double stepSize = 2;
            StdDraw.text(xCenter, yTop, "You find a " + item.toString());
            StdDraw.text(xCenter, yTop -= stepSize, present);
            if (decision) {
                StdDraw.text(xCenter, yTop -= stepSize, "Would you like to equip (e) or discard (d) the item.");
            } else {
                StdDraw.text(xCenter, yTop -= stepSize, "Press (d) to continue.");
            }
            StdDraw.show();
        }

        while (true) {
            if (Engine.id.hasNextChar()) {
                char c = Engine.id.nextChar();
                if (c == 'e' && decision) {
                    equip(item);
                    return;
                }
                if (c == 'd') {
                    return;
                }
            }
        }
    }

    private void equip(Item item) {
        if (item instanceof Helmet) {
            setHelmet((Helmet) item);
        } else if (item instanceof Chestpiece) {
            setChestpiece((Chestpiece) item);
        } else if (item instanceof Leggings) {
            setLeggings((Leggings) item);
        } else if (item instanceof Boots) {
            setBoots((Boots) item);
        } else if (item instanceof Sword) {
            setSword((Sword) item);
        } else {
            setShield((Shield) item);
        }
    }


    public List<String> getStats() {
        List<String> stats = new ArrayList<>();
        stats.add("Player: Level " + getLevel());
        stats.add("Exp: " + exp + " / " + expLevelUp);
        stats.add("Gold: " + getMoney());
        stats.add("Health: " + healthStat + " (+" + (getMaxHealth() - healthStat) + ")" + " = " + currentHealth + "/" + getMaxHealth());
        stats.add("Attack: " + attackStat + " (+" + (getAttack() - attackStat) + ") = " + getAttack());
        stats.add("Health Regen: " + getHealthRegen());
        stats.add("Luck: " + getLuck());
        stats.add("Defense: " + getDefense());
        stats.add("Crit Chance: " + getCritChance() + "%");
        stats.add("Crit Damage: " + getCritDamage() + " x damage");
        stats.add("Parry Chance: " + getParryChance() + "%");
        stats.add("Parry Damage: " + getParryDamage() + "%");
        stats.add("Reflect Damage: " + getReflectDamage() + "%");
        stats.add("Points Remaining: " + getPointsRemaining());
        return stats;
    }

    public void levelUp() {
        exp -= expLevelUp;
        expLevelUp = (int) (expLevelUp * 1.05 + 10);
        level += 1;
        totalPoints += 3;
        addPoints();
    }

    public void addPoints() {

        //Display options available to player;
        while (true) {
            if (Engine.display) {

                StdDraw.clear(new Color(0, 0, 0));
                StdDraw.setFont(new Font("Monaco", Font.BOLD, 24));
                StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT / 1.3, "Current Stats: ");
                List<String> messages = new ArrayList<>();
                messages.add("Health: " + healthStat);
                messages.add("Attack: " + attackStat);
                messages.add("Health Regen: " + healthRegenStat);
                messages.add("");
                messages.add("You have " + getPointsRemaining() + " points available");
                messages.add("By spending 1 point you can:");
                messages.add("Increase health (h) by 20");
                messages.add("Increase attack (a) by 5");
                messages.add("Increase health regen (r) by 1");
                messages.add("");
                messages.add("Press e to exit.");
                Engine.writeMessages(Engine.WIDTH / 2, Engine.HEIGHT / 1.5, messages, 2, new Font("Monaco", Font.BOLD, 18));
                StdDraw.show();
            }
            if (Engine.id.hasNextChar()) {
                char c = Engine.id.nextChar();
                if (getPointsRemaining() > 0) {
                    if (c == 'a') {
                        pointsSpent++;
                        attackStat += 5;
                    } else if (c == 'h') {
                        pointsSpent++;
                        healthStat += 20;
                    } else if (c == 'r') {
                        pointsSpent++;
                        healthRegenStat += 1;
                    }
                }
                if (c == 'e') {
                    return;
                }

            }
            Engine.sleep(15);
        }

    }

    public void flee() {
        Location newLocation = loc.getFloor().getRandomUnoccupiedLocation();
        setLocation(newLocation);
    }


    //Methods to interact with the 2D world
    public TETile getDisplay() {
        return tile;
    }

    public Location getLocation() {
        return loc;
    }

    public void setLocation(Location newLocation) {
        if (newLocation.isPassable() || newLocation.isExit()) {
            if (!newLocation.isOccupied()) {
                loc.setOccupant(null);
                loc = newLocation;
                loc.setOccupant(this);
            } else if (newLocation.isOccupied() && !newLocation.getOccupant().isPlayer()) {
                Combat c = new Combat(this, (Enemy) newLocation.getOccupant());
                if (!c.fled()) {
                    loc.setOccupant(null);
                    loc = newLocation;
                    loc.setOccupant(this);
                }
            }
        }
    }

    public List<String> getDescription() {
        List<String> description = new ArrayList<>();
        description.add("You, the Player.");
        return description;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void move(String dir) {
        Location newLocation = loc.getNeighbor(dir);
        setLocation(newLocation);
    }

    public void die() {
        //ToDo: Figure out what to do with dying... and whether it needs to be implemented here.
        this.loc.setOccupant(null);
        isAlive = false;
        if (Engine.display) {
            StdDraw.clear(new Color(0, 0, 0));
            StdDraw.setFont(new Font("Comic Sans", Font.BOLD, 40));
            StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT / 1.5, "Game Over - You Died!!");
            StdDraw.show();
            Engine.sleep(2000);
            System.exit(0);
        }
    }

    public boolean isPlayer() {
        return true;
    }

    public void passTime() {
        regen();
    }

}

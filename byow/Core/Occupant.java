package byow.Core;

import byow.TileEngine.TETile;
import byow.Util.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.List;
import java.util.Random;

public interface Occupant {
    TETile getDisplay();

    void die();

    List<String> getDescription();

    boolean isPlayer();

    int getMaxHealth();

    int getCurrentHealth();

    int getAttack();

    int getHealthRegen();

    int getLevel();

    int getDefense();

    void setCurrentHealth(int h);

    int getCritChance();

    double getCritDamage();

    Random getRandom();

    void passTime();

    default int attack(Occupant o) {
        Random rand = getRandom();
        double mult = 1;
        if (getRandom().nextDouble() + getCritChance() / 100.0 >= 1) {
            mult = getCritDamage();
            if (Engine.display) {
                StdDraw.clear(new Color(0, 0, 0));
                StdDraw.setFont(new Font("Monaco", Font.BOLD, 50));
                StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT / 2, "Crit!");
                Engine.sleep(500);
            }
        }
        int damage = Math.max(0, (int) (RandomUtils.uniform(rand, .5 * getAttack(), 1.5 * getAttack()) * mult) - o.getDefense());
        o.setCurrentHealth(o.getCurrentHealth() - damage);
        return damage;
    }

    String getName();

    default void regen() {
        setCurrentHealth(Math.min(getCurrentHealth() + getHealthRegen(), getMaxHealth()));
    }
}

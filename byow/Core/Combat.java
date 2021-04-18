package byow.Core;


import edu.princeton.cs.introcs.StdDraw;

import java.util.List;
import java.awt.*;
import java.util.ArrayList;

import static byow.Core.Engine.display;

public class Combat {
    private Enemy enemy;
    private Player player;
    private boolean flee;

    public Combat(Player p, Enemy e) {
        player = p;
        enemy = e;
        flee = false;
        runCombat();
    }

    private void runCombat() {
        String playerMessage = "";
        String enemyMessage = "";
        while (enemy.isAlive() && player.isAlive() && !this.flee) {
            if (Engine.id.hasNextChar()) {
                char c = Engine.id.nextChar();
                boolean passTime = false;

                if (c == 'a') {
                    int damage = player.attack(enemy);
                    playerMessage = "You attack the " + enemy.getName() + " for " + damage + " damage.";
                    passTime = true;
                }
                else if (c == 'h') {
                    int heal = player.heal();
                    playerMessage = "You heal yourself for " + heal + " health.";
                    passTime = true;
                }
                else if (c == 'p'){
                    int damage = player.parry(enemy);
                    if(damage != -1){
                        playerMessage = "You successfully parry the attack.";
                        enemyMessage = "The " + enemy.getName() + "'s attack was parried so it hits itself for " + damage + " damage.";
                        player.regen();
                        enemy.regen();
                    }
                    else{
                        playerMessage = "You fail to parry the attack.";
                        passTime = true;
                    }
                }
                else if (c == 'f'){
                    passTime = true;
                    this.flee = true;
                    playerMessage = "You run away.";
                }

                if (passTime) {
                    int damage = enemy.attack(player);
                    enemyMessage = "The " + enemy.getName() + " attacks you for " + damage + " damage and " + player.reflect(enemy,damage) + " damage is reflected back to it.";
                    player.regen();
                    enemy.regen();
                }

            }
            if (player.getCurrentHealth() <= 0) {
                player.die();
            }
            if (enemy.getCurrentHealth() <= 0) {
                enemy.die();
            }

            if (display) {
                displayCombat(playerMessage, enemyMessage);
            }
            Engine.sleep(15);
        }
        if (flee){
            Engine.sleep(1000);
            enemy.setCurrentHealth(3);
            player.flee();
        }
        else if(player.isAlive()) {
            victory();
        }

    }


    private void victory(){
        int expGained = enemy.getExp(player.getLevel());
        if(display){
            StdDraw.clear(new Color(0,0,0));
            StdDraw.setFont(new Font("Monaco",Font.BOLD,40));
            StdDraw.text(Engine.WIDTH/2,Engine.HEIGHT/1.5,"You kill the " + enemy.getName());
            StdDraw.setFont(new Font("Monaco",Font.BOLD,25));
            StdDraw.text(Engine.WIDTH/2,Engine.HEIGHT/1.5-3,"You get " + expGained + " exp!");
            StdDraw.show();
            Engine.sleep(1000);
        }
        player.gainExp(expGained);
        player.gainItem(enemy.getLoot(player.getLuck()));
    }

    private void displayCombat(String playerMessage, String enemyMessage) {
        StdDraw.clear(new Color(0, 0, 0));
        Font font = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.text(Engine.WIDTH / 2, Engine.HEIGHT / 1.3, "Combat");
        displayCombatant(enemy, Engine.WIDTH / 1.5);
        displayCombatant(player, Engine.WIDTH / 3);
        displayOptions();
        displayOutcome(playerMessage, enemyMessage);
        StdDraw.show();
    }

    private void displayOutcome(String playerMessage, String enemyMessage) {
        if(playerMessage.equals("") || enemyMessage.equals("")){
            return;
        }
        Font font = new Font("Monaco", Font.BOLD, 16);
        StdDraw.setFont(font);
        double xCenter = Engine.WIDTH / 2;
        double yTop = Engine.HEIGHT / 2.5;
        double stepSize = 1.5;
        StdDraw.text(xCenter, yTop, playerMessage);
        StdDraw.text(xCenter, yTop - stepSize, enemyMessage);
        StdDraw.text(xCenter, yTop - stepSize * 2, "You regen " + player.getHealthRegen() + " health.");
        StdDraw.text(xCenter, yTop - stepSize * 3, "The " + enemy.getName() + " regen's " + enemy.getHealthRegen() + " health.");

    }


    private void displayCombatant(Occupant o, double xCenter) {
        Font font = new Font("Monaco", Font.BOLD, 18);
        StdDraw.setFont(font);
        double ytop = Engine.HEIGHT / 1.5;
        double stepSize = 1.5;
        StdDraw.text(xCenter, ytop, "Level " + o.getLevel() + " " + o.getName() + " Stats");
        StdDraw.text(xCenter, ytop - stepSize, "----------------------------------");
        StdDraw.text(xCenter, ytop - stepSize * 2, "Health: " + o.getCurrentHealth() + " / " + o.getMaxHealth());
        StdDraw.text(xCenter, ytop - stepSize * 3, "Attack: " + o.getAttack());
        StdDraw.text(xCenter, ytop - stepSize * 4, "Defense: " + o.getDefense());
        StdDraw.text(xCenter, ytop - stepSize * 5, "Health Regen: " + o.getHealthRegen());
    }

    private void displayOptions() {
        Font font = new Font("Monaco", Font.BOLD, 16);
        List <String> messages = new ArrayList();
        messages.add("Options you have:");
        messages.add("Attack (a): You hit the monster for " + (int) (player.getAttack() * .5) + "-" + (int) (player.getAttack() * 1.5) + " damage.");
        messages.add("Heal (h): You heal yourself for " + (int) (player.getHealthRegen() * 2.5) + "-" + (int) (player.getHealthRegen() * 7.5) + " health.");
        messages.add("Parry (p): You have a " + (player.getParryChance() - 2 * (enemy.getLevel() - player.getLevel())) + "% chance to block all incoming damage, and reflect " + (player.getParryDamage()) + "% of it back to the sender.");
        messages.add("Flee (f): You run away from the combat to a random location on the map, " + "but your opponent gets one more hit against you.");
        Engine.writeMessages(Engine.WIDTH/2, Engine.HEIGHT/4,messages,1.5, font);
    }

    public boolean fled(){
        return this.flee;
    }
}

package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;


import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 39;
    public static final int HUDHEIGHT = 3;
    public static final int WORLDWIDTH = WIDTH;
    public static final int WORLDHEIGHT = HEIGHT - HUDHEIGHT;
    private Random random;
    private Player player;
    private Floor floor;
    public static InputDevice id;
    public static boolean display;
    private String lastSave;
    private boolean stop;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        this.stop = false;
        ter.initialize(WIDTH, HEIGHT);
        this.id = new HumanInputDevice();
        this.display = true;
        long seed = initializeGame();
        this.random = new Random(seed);
        this.player = new Player(new Location(Tileset.NOTHING, null, false), random);
        this.floor = new Floor(random, WORLDWIDTH, WORLDHEIGHT, player, 1);
        runGame();
        System.exit(0);
    }

    private long initializeGame() {
        if (display) {
            drawMenu();
        }
        while (true) {
           if (id.hasNextChar()) {
                char c = id.nextChar();
                if (c == 'n') {
                    return getSeed();
                }
                if (c == 'q') {
                    System.exit(0);
                }
                if (c == 'l') {
                    loadGame();
                }
            }
        }
    }

    private long getSeed() {
        long seed = 0;
        while (true) {
            if(display){
                displaySeed(seed);
            }
            if (id.hasNextChar()) {
                char c = id.nextChar();
                if (c == 's') {
                    return seed;
                }
                int digit = Character.getNumericValue(c);
                seed = seed * 10 + digit;
            }
            sleep(15);
        }
    }

    private void displaySeed(long seed) {
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.text(WIDTH / 2, ((double) HEIGHT * 2) / 3, "Input your seed, press s to submit.");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "" + seed);
        StdDraw.show();
    }

    private void loadGame() {
        try {
            Scanner sc = new Scanner(new File("save.txt"));
            this.lastSave = sc.next();
            id.addInputs(lastSave);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveGame() {
        File f = new File("save.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            PrintWriter out = new PrintWriter(f);
            out.print(id.getAllInputs());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawMenu() {
        double x = WIDTH / 2;
        double titleY = ((double) HEIGHT * 3) / 4;
        double centerY = ((double) HEIGHT) / 2;
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(new Color(255, 255, 255));
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 40));
        StdDraw.text(x, titleY, "Main Menu");
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
        StdDraw.text(x, centerY, "New Game (N)");
        StdDraw.text(x, centerY - 3, "Load Game (L)");
        StdDraw.text(x, centerY - 6, "Quit (Q)");
        StdDraw.show();
    }


    private void runGame() {
        char prev = 'u';
        int delay = 15;
        while (!stop) {
            if (id.hasNextChar()) {
                prev = oneCharInput(id.nextChar(), prev);
                delay = 1;
            }
            if (display) {
                displayBoard();
            }
            if (!player.isAlive() || id.inputEnded()) {
                stop = true;
                return;
            }

            sleep(delay);
            delay = 15;

        }
    }

    private void displayBoard() {
        ter.renderFrame(floor.getDisplay());
        StdDraw.setPenColor(new Color(255, 255, 255));
        drawDescription();
        drawPlayerStats();
        StdDraw.show();
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < WORLDWIDTH && y < WORLDHEIGHT && y >= 0;
    }


    private void drawPlayerStats() {
        List<String> descriptions = player.getStats();
        int xStart = WIDTH / 4;
        for (int i = 0; i < descriptions.size(); i += 3) {
            List<String> description = descriptions.subList(i, Math.min(i + 3, descriptions.size()));
            drawToHud(description, xStart);
            xStart += WIDTH / 8;
        }
    }


    private void drawDescription() {
        int mouseY = (int) StdDraw.mouseY();
        int mouseX = (int) StdDraw.mouseX();
        if (inBounds(mouseX, mouseY)) {
            Location mouse = floor.getLocation(new Position(mouseX, mouseY));
            drawToHud(mouse.getDescription(), 1);
        }
    }

    public static void drawToHud(List<String> messages, double xLeft) {
        int steps = messages.size();
        double stepSize = ((double) HUDHEIGHT) / (steps + 1);
        double startY = WORLDHEIGHT + HUDHEIGHT - stepSize;
        int fontSize = (int) ((14 * 3) / ((double) (Math.max(steps + 1, 4))));
        StdDraw.setFont(new Font("Monaco", Font.BOLD, fontSize));
        for (String desc : messages) {
            StdDraw.textLeft(xLeft, startY, desc);
            startY -= stepSize;
        }
    }


    public static void sleep(long milliSeconds) {
        if(display){
            try {
                Thread.sleep(milliSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private char oneCharInput(char c, char prev) {
        boolean passTime = true;
        if (c == 'w') {
            player.move("North");
        } else if (c == 's' && prev == ':') {
            passTime = false;
            saveGame();
        } else if (c == 's') {
            player.move("South");
        } else if (c == 'a') {
            player.move("West");
        } else if (c == 'd') {
            player.move("East");
        } else if (c == ':') {
            passTime = false;
        } else if (c == 'q' && prev == ':') {
            saveGame();
            stop = true;
            passTime = false;
        }

        if (passTime) {
            this.floor = floor.simulateOneTurn();
        }
        return c;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TOD Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        this.display = false;
        this.stop = false;
        this.id = new StringInputDevice();
        id.addInputs(input.toLowerCase());
        long seed = initializeGame();
        this.random = new Random(seed);
        this.player = new Player(new Location(Tileset.NOTHING, null, false), random);
        this.floor = new Floor(random, WIDTH, HEIGHT, player, 1);
        runGame();
        TETile[][] finalWorldFrame = floor.getDisplay();
        return finalWorldFrame;
    }





    public static void writeMessages(double xCenter, double yTop, List<String> messages, double stepSize, Font font){
        StdDraw.setFont(font);
        for (String message : messages) {
            StdDraw.text(xCenter, yTop, message);
            yTop -= stepSize;
        }
    }
}

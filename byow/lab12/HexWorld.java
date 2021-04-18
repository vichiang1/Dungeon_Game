package byow.lab12;

import org.junit.Test;

import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    private static TETile[][] world;
    private static final Random RANDOM = new Random();

    private static class Position {
        public int x;
        public int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    //If Center is a square instead of a point, it is the top left corner of the square
    private static void drawHexagon(Position center, int sideLength, TETile tileType) {
        Position startPosition = findStartForHex(center, sideLength);
        int xStart = startPosition.x;
        int yStart = startPosition.y;
        int curLength = sideLength;
        for (int y = yStart; y > yStart - sideLength; y--) {
            Position p = new Position(xStart, y);
            drawRow(new Position(xStart, y), curLength, tileType);
            xStart -= 1;
            curLength += 2;
        }
        yStart -= sideLength;
        for (int y = yStart; y > yStart - sideLength; y--) {
            xStart += 1;
            curLength -= 2;
            drawRow(new Position(xStart, y), curLength, tileType);

        }
    }

    private static void drawRow(Position start, int sideLength, TETile tileType) {
        for (int x = start.x; x < start.x + sideLength; x++) {
            world[x][start.y] = tileType;
        }
    }

    private static void createEmptyWorld() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private static Position findStartForHex(Position center, int sideLength) {
        int xStart = center.x - (sideLength - 1) / 2;
        int yStart = center.y + (sideLength - 1);
        return new Position(xStart, yStart);
    }

    private static void drawColOfHexagonsRandom(Position centerTop, int numHexes, int sideLength) {
        for (int y = centerTop.y; y > centerTop.y - 2 * numHexes * sideLength; y -= 2 * sideLength) {
            drawHexagon(new Position(centerTop.x, y), sideLength, getRandomBiome());
        }
    }

    private static List<Position> startPositions(Position centerTop, int numCols, int sideLength) {
        List<Position> positions = new ArrayList<>();
        positions.add(centerTop);
        int curX = centerTop.x;
        int curY = centerTop.y;
        for (int i = 0; i < numCols / 2; i++) {
            curY -= sideLength;
            curX = curX - sideLength - (sideLength + 1) / 2;
            positions.add(0, new Position(curX, curY));
        }
        curX = centerTop.x;
        curY = centerTop.y;
        for (int i = 0; i < numCols / 2; i++) {
            curX = curX + sideLength + (sideLength + 1) / 2;
            curY -= sideLength;
            positions.add(new Position(curX, curY));
        }
        return positions;
    }

    public static void drawHexOfHexes(Position centerTop, int bigSideLength, int smallSideLength) {
        List<Position> positions = startPositions(centerTop, bigSideLength * 2 - 1, smallSideLength);
        int numHexes = bigSideLength;
        int i = 1;
        for (Position p : positions) {
            drawColOfHexagonsRandom(p, numHexes, smallSideLength);
            if (i < bigSideLength) {
                numHexes += 1;
            } else {
                numHexes -= 1;
            }
            i++;
        }
    }

    public static TETile getRandomBiome() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0:
                return Tileset.MOUNTAIN;
            case 1:
                return Tileset.GRASS;
            case 2:
                return Tileset.TREE;
            case 3:
                return Tileset.FLOWER;
            case 4:
                return Tileset.SAND;
            default:
                return Tileset.NOTHING;
        }
    }

    public static void main(String[] args) {
        world = new TETile[WIDTH][HEIGHT];
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        createEmptyWorld();
        drawHexOfHexes(new Position(20, 35), 3, 3);
        ter.renderFrame(world);
    }
}

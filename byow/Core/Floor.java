package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Util.*;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Floor {
    private Location[][] map;
    private int floorSize;
    private Random random;
    private int spacesFilled;
    private Map<Location, Position> locToPos;
    private static final ArraySet<TETile> BARRIERS =
            new ArraySet<>(Arrays.asList(Tileset.WALL, Tileset.MOUNTAIN, Tileset.TREE));
    private static final ArraySet<TETile> GROUNDS =
            new ArraySet<>(Arrays.asList(Tileset.FLOOR, Tileset.GRASS, Tileset.SAND));
    private ArraySet<Location> groundTiles;
    private ArraySet<Location> barrierTiles;
    private TETile BARRIER = Tileset.WALL;
    private TETile GROUND = Tileset.FLOOR;
    private Map<String, Position> directions;
    Player player;
    private ArraySet<Enemy> enemies;
    private Stairs stairsDown;
    private Stairs stairsUp;
    private LocationGraph groundGraph;
    private int floorLevel;
    private Map<String, Floor> neighboringFloors;
    private Map<String,String> opposites;
    private Position playerPosition;

    private static final int MINHALLWAYLENGTH = 25;
    private static final int MAXHALLWAYLENGTH = 80;
    private static final int MINROOMLENGTH = 7;
    private static final int MAXROOMLENGTH = 12;
    private static final int BASEMINENEMIES = 8;
    private static final int BASEMAXENEMIES = 15;
    private static final int GUARENTEDLOWENEMIES = 3;
    private double hallwayPercentage = .4;
    private double totalPercentage = .55;


    //@Todo dont forget to put seed into the random call later
    public Floor(Random rand, int width, int height, Player player, int level) {
        random = rand;
        map = new Location[width][height];
        floorSize = width * height;
        groundTiles = new ArraySet();
        barrierTiles = new ArraySet();
        locToPos = new HashMap();
        directions = new HashMap();
        opposites = new HashMap<>();
        neighboringFloors = new HashMap<>();
        enemies = new ArraySet<>();
        this.floorLevel = level;
        directions.put("West", new Position(-1, 0));
        directions.put("North", new Position(0, 1));
        directions.put("South", new Position(0, -1));
        directions.put("East", new Position(1, 0));
        opposites.put("North","South");
        opposites.put("South","North");
        opposites.put("East","West");
        opposites.put("West","East");
        opposites.put("Up","Down");
        opposites.put("Down","Up");
        this.player = player;
        initializeFloor();
        initializeGroundGraph();
        playerPosition = locToPos.get(player.getLocation());
    }


    //Public Methods

    /*
    Returns a TETile[][] that can be read in by the visualizer.
     */
    public TETile[][] getDisplay() {
        TETile[][] out = new TETile[map.length][map[0].length];
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                out[row][col] = map[row][col].getDisplay();
            }
        }
        return out;
    }



    public Location getNeighbor(Location loc, String dir) {
        Assert.assertTrue(directions.containsKey(dir), "Invalid Direction, Must be: North, West, South, East");
        Assert.assertTrue(locToPos.containsKey(loc), "Location not found in this floor");
        Position p = locToPos.get(loc);
        Position offset = directions.get(dir);
        return getLocation(p.add(offset));
    }


    public ArraySet<Location> getNeighbors(Location loc) {
        ArraySet<Location> neighbors = new ArraySet();
        for (String dir : directions.keySet()) {
            neighbors.add(getNeighbor(loc, dir));
        }
        return neighbors;
    }

    public double estimateDistance(Location one, Location two) {
        Position p1 = locToPos.get(one);
        Position p2 = locToPos.get(two);
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    /*
    Advances time in the world 1 turn, based on the input of the player represented as a char.
    Returns the current floor the game should be running on (useful when player changes floor).
     */
    public Floor simulateOneTurn() {
        Floor active_floor = this;
        List<Enemy> dead = new ArrayList<>();
        for (Object e : enemies) {
            Enemy enemy = (Enemy) e;
            if (enemy.isAlive()) {
                enemy.passTime();
            } else {
                dead.add(enemy);
            }
        }
        player.passTime();
        for (Enemy e : dead) {
            enemies.remove(e);
        }
        stairsDown.setLocked(enemies.size() > 0);
        playerPosition = locToPos.get(player.getLocation());
        if (player.getLocation().isExit()) {
            Exit e = (Exit) player.getLocation();
            active_floor = neighboringFloors.get(e.direction);
            if(active_floor == null){
                if(e.equals(stairsDown)) {
                    if(floorLevel == 2){
                        victory();
                    }
                    active_floor = new Floor(random, map.length, map[0].length, player, floorLevel + 1);
                    active_floor.stairsUp.setLocked(false);
                }
                else {
                    active_floor = new Floor(random,map.length,map[0].length,player,floorLevel);
                    Position o = locToPos.get(e);
                    active_floor.playerPosition = new Position(Math.min(Engine.WORLDWIDTH - o.x,Engine.WORLDWIDTH-1), Math.min(Engine.WORLDHEIGHT - o.y,Engine.WORLDHEIGHT-1)).add(directions.get(e.getDirection()));
                }
                setNeighbor(e.getDirection(),active_floor);
                active_floor.setNeighbor(opposites.get(e.getDirection()),this);
            }

            playerPosition = playerPosition.add(directions.getOrDefault(opposites.get(e.getDirection()),new Position(0,0)));
            player.setLocation(active_floor.getLocation(active_floor.playerPosition));
        }
        return active_floor;
    }

    private void victory(){
        if(Engine.display){
            StdDraw.clear(new Color(0,0,0));
            StdDraw.setFont(new Font("Comic Sans",Font.BOLD,50));
            StdDraw.text(Engine.WIDTH/2,Engine.HEIGHT/1.5,"You WIN (FOR NOW)!");
            StdDraw.setFont(new Font("Comic Sans",Font.BOLD,30));
            StdDraw.text(Engine.WIDTH/2,Engine.HEIGHT/1.5-10,"You end the game with: " + player.getMoney() + " gold/points.");
            StdDraw.show();
        }
        Engine.sleep(10000);
        System.exit(0);
    }

    private void setNeighbor(String direction, Floor f){
        this.neighboringFloors.put(direction,f);
    }


    public double distanceToPlayer(Location start) {
        if(!groundGraph.contains(player.getLocation())){
            return Float.POSITIVE_INFINITY;
        }
        return new AStarSolver<Location>(groundGraph, start, player.getLocation(), 10).solutionWeight();
    }

    public Location nextStepTowardsPlayer(Location start) {
        List<Location> solution = new AStarSolver<Location>(groundGraph, start, player.getLocation(), 10).solution();
        if (solution.size() > 1) {
            return solution.get(1);
        }
        return solution.get(0);
    }

    public Location getRandomUnoccupiedLocation(){
        Location loc = groundTiles.getRandom(random);
        while(loc.isOccupied()){
            loc = groundTiles.getRandom(random);
        }
        return loc;
    }


    //@Warning: Will break if pos is out of the map
    public Location getLocation(Position pos) {
        return map[pos.x][pos.y];
    }


    //Private Methods for World Generation


    private void initializeGroundGraph() {
        List<List<Location>> locations = new ArrayList();
        for (Object o : groundTiles) {
            Location source = (Location) o;
            List<Location> locs = new ArrayList<>();
            locs.add(source);
            for (Object o2 : source.getPassableNeighbors()) {
                locs.add((Location) o2);
            }
            locations.add(locs);
        }
        this.groundGraph = new LocationGraph(this, locations);
    }

    private void initializeFloor() {
        initializeEmptyFloor();
        generateHallways();
        generateRooms(this.groundTiles.copy());
        addExits();
        this.player.setLocation(groundTiles.getRandom(random));
        addEnemies();
    }

    //Ignores abstraction barrier to always use placeTile to place a tile
    private void addExits() {
        stairsDown = new Stairs(this,"Down");
        Location ld = groundTiles.getRandom(random);
        Location lu = groundTiles.getRandom(random);
        Position sd = locToPos.get(ld);
        Position su = locToPos.get(lu);
        replaceLocation(sd,stairsDown,true);
        if(this.floorLevel>1){
            stairsUp = new Stairs(this,"Up");
            replaceLocation(su,stairsUp,true);
        }

        replaceLocation(new Position(0,Engine.WORLDHEIGHT/2),new Exit(Tileset.UNLOCKED_DOOR,this,false,"West"),false);
        replaceLocation(new Position(Engine.WORLDWIDTH-1,Engine.WORLDHEIGHT/2),new Exit(Tileset.UNLOCKED_DOOR,this,false,"East"),false);
        replaceLocation(new Position(Engine.WORLDWIDTH/2,0),new Exit(Tileset.UNLOCKED_DOOR,this,false,"South"),false);
        replaceLocation(new Position(Engine.WORLDWIDTH/2,Engine.WORLDHEIGHT-1),new Exit(Tileset.UNLOCKED_DOOR,this,false,"North"),false);

    }


    //Replaces the location at position p with newLocation
    private void replaceLocation(Position p, Location newLocation,boolean ground){
        Location oldLocation = getLocation(p);
        locToPos.remove(oldLocation);
        locToPos.put(newLocation,p);
        map[p.x][p.y] = newLocation;
        groundTiles.remove(oldLocation);
        barrierTiles.remove(oldLocation);
        if(ground){
            groundTiles.add(newLocation);
        } else{
            barrierTiles.add(newLocation);
        }

    }


    private void addEnemies() {
        int num = RandomUtils.uniform(random, BASEMINENEMIES, BASEMAXENEMIES);
        int low = 0;
        for (int i = 0; i < num; i++) {
            Location loc = groundTiles.getRandom(random);
            while (loc.isOccupied()) {
                loc = groundTiles.getRandom(random);
            }
            if(low<GUARENTEDLOWENEMIES){
                if(this.floorLevel == 1){
                    enemies.add(new Bat(loc,random,1));
                }else{
                    enemies.add(new Spider(loc,random,6));
                }

                low++;
            }
            else{
                if(this.floorLevel == 1){
                    enemies.add(new Bat(loc, random));
                }else{
                    enemies.add(new Spider(loc,random));
                }

            }

        }
    }

    /*
    Initializes the floor with all empty tiles, except the edges which get barrier tiles
    @Warning: only method that doesn't follow the
    abstraction to only add tiles through the place Tile method.
     */
    private void initializeEmptyFloor() {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (nearEdge(new Position(row, col))) {
                    map[row][col] = new Location(BARRIER, this, false);
                } else {
                    map[row][col] = new Location(Tileset.NOTHING, this, false);
                }
                locToPos.put(map[row][col], new Position(row, col));
            }
        }
    }


    private void generateHallways() {
        drawRandomHallway(new Position(map.length / 2, map[0].length / 2));
        while (floorSize * hallwayPercentage > spacesFilled) {
            drawRandomHallway(locToPos.get(groundTiles.getRandom(random)));
        }
        drawHallway(new Position(0,Engine.WORLDHEIGHT/2),1,0,Engine.WORLDWIDTH);
        drawHallway(new Position(Engine.WORLDWIDTH/2,0),0,1,Engine.WORLDHEIGHT);
    }


    private void drawRandomHallway(Position startPosition) {
        int xChange = RandomUtils.uniform(random, -1, 2);
        int yChange = 0;
        if (xChange == 0) {
            if (RandomUtils.bernoulli(random)) {
                yChange = 1;
            } else {
                yChange = -1;
            }
        }
        if (RandomUtils.bernoulli(random)) {
            int temp = yChange;
            yChange = xChange;
            xChange = temp;
        }
        drawHallway(startPosition, xChange, yChange,
                RandomUtils.uniform(random, MINHALLWAYLENGTH, MAXHALLWAYLENGTH + 1));
    }


    private void generateRooms(ArraySet<Location> validLocations) {
        while (totalPercentage * floorSize > spacesFilled) {
            drawRandomRoomCenter(locToPos.get(validLocations.getRandom(random)));
        }
    }


    private void drawRandomRoomCenter(Position centerPosition) {
        drawRoom(centerPosition, RandomUtils.uniform(random, MINROOMLENGTH, MAXROOMLENGTH + 1),
                RandomUtils.uniform(random, MINROOMLENGTH, MAXROOMLENGTH + 1));
    }


    /*
    Draws a line of tile type tileType, starting at position start.
    @Params
    start --> Where the line starts
    xChange --> How much the x coordinate changes with each "step"
    yChange --> How much the y coordinate changes with each "step"
    sideLength --> The Number of "steps" the code takes
    tileType --> The type of tile the code places on the line
    safePlacement --> (boolean) if true, will only place a
        tile if there was no other title to begin with.
     */
    private void drawLine(Position start, int xChange, int yChange,
                          int sideLength, TETile tileType, boolean safePlacement) {
        int x = start.x;
        int y = start.y;
        for (int i = 0; i < sideLength; i++) {
            placeTile(new Position(x, y), tileType, safePlacement);
            x += xChange;
            y += yChange;
        }
    }

    /*
        Abstraction Barrier: All editing of the map will use this method
        (Once the map is initialized as all being nothing).
        Changes the tile at position p into tileType if it is a valid placement.
        @Params
        p --> The location to be changed
        tileType --> the type to change the tile into
        safePlacement --> Boolean, if true will only
            place the tile if there isn't a tile there already.
        @TODO Clean up some of this code if possible. (Starting to be a lot of checks)
     */
    private void placeTile(Position p, TETile tileType, boolean safePlacement) {
        if (!nearEdge(p) && (!safePlacement || Tileset.NOTHING.equals(map[p.x][p.y].getTile()))) {
            Location loc = map[p.x][p.y];
            boolean passable = false;
            if (Tileset.NOTHING.equals(loc.getTile())) {
                this.spacesFilled += 1;
            }
            loc.setTile(tileType);
            if (GROUNDS.contains(tileType)) {
                passable = true;
                groundTiles.add(loc);
                barrierTiles.remove(loc);
            } else if (BARRIERS.contains(tileType)) {
                groundTiles.remove(loc);
                barrierTiles.add(loc);
            }
            loc.setPassable(passable);

        }
    }

    /*
        Takes in a position and returns whether that position
        is in the outer edge of the world or off the world.
     */
    private boolean nearEdge(Position p) {
        return p.x <= 0 || p.x >= map.length - 1 || p.y <= 0 || p.y >= map[0].length - 1;
    }

    /*
    Makes a hallway starting at StartPosition, either vertical or horizontal,
    as deterimined by changeX and changeY
     */
    private void drawHallway(Position startPosition, int changeX, int changeY, int sideLength) {
        Assert.assertTrue(Math.abs(changeX) + Math.abs(changeY) == 1,
                "Hallways must be vertical or horizontal");
        drawLine(new Position(startPosition.x + changeY,
                startPosition.y + changeX), changeX, changeY, sideLength, BARRIER, true);
        drawLine(new Position(startPosition.x + changeY,
                startPosition.y + changeX), -changeY, -changeX, 3, BARRIER, true);
        drawLine(new Position(startPosition.x + changeY + changeX * (sideLength - 1),
                        startPosition.y + changeX + changeY * (sideLength - 1)),
                -changeY, -changeX, 3, BARRIER, true);

        drawLine(new Position(startPosition.x + changeX, startPosition.y + changeY),
                changeX, changeY, sideLength - 2, GROUND, false);
        drawLine(new Position(startPosition.x - changeY, startPosition.y - changeX),
                changeX, changeY, sideLength, BARRIER, true);
    }

    private void drawEdges(Position topLeft, int xLength, int yLength,
                           TETile tileType, boolean safePlacement) {
        Position bottomRight = new Position(topLeft.x + xLength - 1, topLeft.y - yLength + 1);
        drawLine(topLeft, 1, 0, xLength, tileType, safePlacement);
        drawLine(topLeft, 0, -1, yLength, tileType, safePlacement);
        drawLine(bottomRight, -1, 0, xLength, tileType, safePlacement);
        drawLine(bottomRight, 0, 1, yLength, tileType, safePlacement);
    }

    private void drawRoom(Position center, int xLength, int yLength) {
        Position topLeft = new Position(center.x - xLength / 2, center.y + yLength / 2);
        drawEdges(topLeft, xLength, yLength, BARRIER, true);
        for (int y = topLeft.y - 1; y > topLeft.y - yLength + 1; y--) {
            drawLine(new Position(topLeft.x + 1, y), 1, 0,
                    xLength - 2, GROUND, false);
        }
    }


}

package byow.Core;

import byow.TileEngine.TETile;
import byow.Util.Assert;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private Occupant occupant;
    private TETile tile;
    private Floor floor;
    private boolean passable;

    public Location(TETile tile,Floor floor,boolean passable) {
        this.tile = tile;
        this.floor = floor;
        this.passable = passable;
    }



    public TETile getDisplay() {
        if (occupant == null) {
            return tile;
        }
        return occupant.getDisplay();
    }


    public List<String> getDescription(){
        List<String> description = new ArrayList<>();
        description.add("Tile: " + getTile().description());
        if(isOccupied()){
            String d = "Occupant: ";
            for(String desc: occupant.getDescription()){
                description.add(d+ desc);
                d = "";
            }
        }
        return description;
    }

    public double distanceToPlayer(){
        return floor.distanceToPlayer(this);
    }
    public Location nextStepTowardPlayer(){
        return floor.nextStepTowardsPlayer(this);
    }

    public Floor getFloor(){
        return floor;
    }




    /*
    Returns a ArraySet of all locations adjacent to this one. Such that:
        The location is "passable".
     */
    public ArraySet<Location> getPassableNeighbors(){
        ArraySet<Location> passableLocations = new ArraySet();
        for(Object o: getNeighbors()){
            Location loc = (Location) o;
            if(loc.passable){
                passableLocations.add(loc);
            }
        }
        return passableLocations;
    }

    public ArraySet<Location> getUnoccupiedNeighbors(boolean playerAllowed){
        ArraySet<Location> unoccupiedLocations = new ArraySet();
        for(Object o: getPassableNeighbors()){
            Location loc = (Location) o;
            if(!loc.isOccupied() || (playerAllowed && loc.occupant.isPlayer())){
                unoccupiedLocations.add(loc);
            }
        }
        return unoccupiedLocations;
    }

    boolean isExit(){
        return false;
    }



    //Basic Getter and Setter Methods


    public void setPassable(boolean passable){
        this.passable = passable;
    }

    public boolean isOccupied(){
        return occupant != null;
    }
    public void setTile(TETile input) {
        this.tile = input;
    }

    public Location getNeighbor(String dir){
        return floor.getNeighbor(this,dir);
    }

    public ArraySet<Location> getNeighbors(){
        return floor.getNeighbors(this);
    }
    public boolean isPassable(){
        return passable;
    }
    public void setOccupant(Occupant occ){
        this.occupant = occ;
    }
    public TETile getTile() {
        return tile;
    }
    public Occupant getOccupant(){
        return occupant;
    }
}

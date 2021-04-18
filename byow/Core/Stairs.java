package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;

public class Stairs extends Exit {

    private boolean locked;

    public Stairs(Floor floor,String direction) {
        super(Tileset.LOCKED_STAIR,floor,true,direction);
        locked = true;
    }

    void setLocked(boolean value){
        this.locked = value;
    }

    @Override
    boolean isExit(){
        return !locked;
    }
    @Override
    public TETile getDisplay(){
        if(isOccupied()){
            return getOccupant().getDisplay();
        }
        return getTile();
    }
    @Override
    public TETile getTile(){
        if(locked){
            return Tileset.LOCKED_STAIR;
        }
        else {
            return Tileset.UNLOCKED_STAIR;
        }
    }

    public List<String> getDescription(){
        List<String> description = new ArrayList<>();
        if(this.direction == "Up"){
            description.add("Tile: A staircase winding up to the floor above.");
        }
        else{
            description.add("Tile: " + getTile().description());
        }
        if(isOccupied()){
            String d = "Occupant: ";
            for(String desc: getOccupant().getDescription()){
                description.add(d+ desc);
                d = "";
            }
        }
        return description;
    }

}

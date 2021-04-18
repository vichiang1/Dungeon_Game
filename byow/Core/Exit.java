package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Exit extends Location {
    String direction;
    public Exit(TETile tile, Floor floor, boolean passable,String direction) {
        super(tile, floor, passable);
        this.direction = direction;
    }

    boolean isExit(){
        return true;
    }

    public String getDirection(){
        return this.direction;
    }
}

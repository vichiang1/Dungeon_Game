package byow.Core;

public class Position {
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position add(Position other) {
        return new Position(this.x + other.x, this.y + other.y);
    }


}

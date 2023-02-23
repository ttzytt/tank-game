package utils;
import java.awt.*;

public class Coord {
    public float x, y;
    public Coord(){
        this.x = 0;
        this.y = 0;
    }

    public Coord(Direct dir, float val){
        switch (dir){
            case UP:
                this.x = 0;
                this.y = -val;
                break;
            case DOWN:
                this.x = 0;
                this.y = val;
                break;
            case LEFT:
                this.x = -val;
                this.y = 0;
                break;
            case RIGHT:
                this.x = val;
                this.y = 0;
                break;
            default:
                assert false : "Invalid direction";
        }
    }

    public static Rectangle toRect(Coord pos, Coord size){
        return new Rectangle((int)pos.x, (int)pos.y, (int)size.x, (int)size.y);
    }

    public Coord(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Coord(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Coord(Coord c){
        this.x = c.x;
        this.y = c.y;
    }

    public Coord add(Coord c){
        return new Coord(x + c.x, y + c.y);
    }

    public Coord sub(Coord c){
        return new Coord(x - c.x, y - c.y);
    }

    public Coord mul(long n){
        return new Coord(x * n, y * n);
    }

    public Coord mul(float n){
        return new Coord(x * n, y * n);
    }

    public Coord div(long n){
        return new Coord(x / n, y / n);
    }

    public Coord div(float n){
        return new Coord(x / n, y / n);
    }

    public Coord mod(long n){
        return new Coord(x % n, y % n);
    }

    public Coord abs(){
        return new Coord(Math.abs(x), Math.abs(y));
    }

    public Coord turn(Direct dir) {
        assert (x == 0 || y == 0) : "Cannot move both x and y at the same time, this coord might be a position value";
        switch (dir){
            case UP:
                return new Coord(x, -Math.abs(y));
            case DOWN:
                return new Coord(x, Math.abs(y));
            case LEFT:
                return new Coord(-Math.abs(x), y);
            case RIGHT:
                return new Coord(Math.abs(x), y);
            default:
                assert false : "Invalid direction";
                return null;
        }
    }

    public float dis_man(Coord c){
        // Manhattan distance
        return Math.abs(x - c.x) + Math.abs(y - c.y);
    }

    public Coord round(){
        return new Coord(Math.round(x), Math.round(y));
    }

    public static boolean inRect(Coord pos, Coord size, Coord rectPos, Coord rectSize){
        return pos.x >= rectPos.x && pos.y >= rectPos.y && pos.x + size.x <= rectPos.x + rectSize.x && pos.y + size.y <= rectPos.y + rectSize.y;
    }

    public boolean inRect(Coord size, Coord rectPos, Coord rectSize){  
        return inRect(this, size, rectPos, rectSize);
    }
    
    public static Coord zero(){
        return new Coord(0, 0);
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}

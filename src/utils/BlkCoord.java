package utils;

public class BlkCoord {
    public int x, y;

    public BlkCoord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public BlkCoord getAdj(Direct dir, int cnt) {
        switch (dir) {
            case UP:
                return new BlkCoord(x, y - cnt);
            case DOWN:
                return new BlkCoord(x, y + cnt);
            case LEFT:
                return new BlkCoord(x - cnt, y);
            case RIGHT:
                return new BlkCoord(x + cnt, y);
        }
        return null;
    }

}
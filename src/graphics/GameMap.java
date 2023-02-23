package graphics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import gameElements.*;
import gameElements.GameElement.RemoveStat;
import utils.*;
import java.util.*;
import java.util.Hashtable;

public class GameMap {
    private Tank tk1, tk2;
    private GameElement[][] map;
    private String srcPath;
    private int max_x, max_y;

    private Hashtable<Blks, BlkCoord> blkToPos = new Hashtable<>();

    public GameMap(String mapName) {
        this.srcPath = mapName;
        FileInputStream fin;
        InputStreamReader reader;
        BufferedReader buf_reader;
        try {
            fin = new FileInputStream(Consts.MAP_FOLDER + mapName);
            reader = new InputStreamReader(fin);
            buf_reader = new BufferedReader(reader);
            parseMap(buf_reader);
            Consts.mapSize = new Coord(max_x, max_y);
        } catch (Exception e) {
            System.out.println("excetion thrown when loading map: " + e.getMessage());
        }
    }

    public ArrayList<GameElement> getEles() {
        ArrayList<GameElement> res = new ArrayList<>();
        for (int i = 0; i < max_x; i++) {
            for (int j = 0; j < max_y; j++) {
                if (map[i][j] != null && !(map[i][j] instanceof EmptyBlk)) {
                    res.add(map[i][j]);
                    ArrayList<GameElement> subEles = map[i][j].getAllSubEle();
                    if (subEles != null) {
                        res.addAll(subEles);
                    }
                }
            }
        }
        if (tk1 != null && tk1.getRemoveStat() != RemoveStat.REMED) {
            res.add(tk1);
            res.addAll(tk1.getAllSubEle());
        } else {
            tk1 = null;
        }
        if (tk2 != null && tk2.getRemoveStat() != RemoveStat.REMED) {
            res.add(tk2);
            res.addAll(tk2.getAllSubEle());
        } else {
            tk2 = null;
        }
        return res;
    }
    public GameElement getBlk(BlkCoord pos){
        return map[pos.x][pos.y];
    }

    public GameElement getBlk(int x, int y) {
        return map[x][y];
    }

    public void setMap(Blks[][] map) {
        this.map = map;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public GameElement[][] getMap() {
        return map;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public int getMax_x() {
        return max_x;
    }

    public void setMax_x(int max_x) {
        this.max_x = max_x;
    }

    public int getMax_y() {
        return max_y;
    }

    public void setMax_y(int max_y) {
        this.max_y = max_y;
    }

    public String getMAP_FOLDER() {
        return Consts.MAP_FOLDER;
    }

    public void remBlk(Blks blk) {
        BlkCoord pos = blkToPos.get(blk);
        blkToPos.remove(blk);
        map[pos.x][pos.y] = null;
    }

    private void parseMap(BufferedReader bufr) {
        String curLine;
        try {
            // read the header
            String[] sizeStr = bufr.readLine().split("[*,\s]+", 0);
            max_x = Integer.parseInt(sizeStr[0]);
            max_y = Integer.parseInt(sizeStr[1]);
            map = new GameElement[max_x + 1][max_y + 1];
            int cur_y = 0;
            while ((curLine = bufr.readLine()) != null) {
                for (int cur_x = 0; cur_x < max_x; cur_x++) {
                    switch (curLine.charAt(cur_x)) {
                        case 'O':
                            break;
                        case 'D':
                            map[cur_x][cur_y] = new DestrBlk(new BlkCoord(cur_x, cur_y));
                            // top left corner, so + 1 for y
                            break;
                        case 'S':
                            map[cur_x][cur_y] = new SolidBlk(new BlkCoord(cur_x, cur_y));
                            break;
                        case '1':
                            tk1 = new Tank(new Coord(cur_x, cur_y), 1);
                            break;
                        case '2':
                            tk2 = new Tank(new Coord(cur_x, cur_y), 2);
                            map[cur_x][cur_y] = new EmptyBlk(new BlkCoord(cur_x, cur_y));
                            break;
                    }
                    if (map[cur_x][cur_y] != null)
                    map[cur_x][cur_y].setMap(this);
                    if (map[cur_x][cur_y] instanceof Blks)
                        blkToPos.put((Blks) map[cur_x][cur_y], new BlkCoord(cur_x, cur_y));
                }
                cur_y++;
            }
        } catch (Exception e) {
            System.out.println("exception thrown when parsing map: " + e.getMessage());
        }
    }

    public Tank getTk1() {
        return tk1;
    }

    public void setTk1(Tank tk1) {
        this.tk1 = tk1;
    }

    public Tank getTk2() {
        return tk2;
    }

    public void setTk2(Tank tk2) {
        this.tk2 = tk2;
    }

    public void setMap(GameElement[][] map) {
        this.map = map;
    }
}
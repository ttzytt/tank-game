package graphics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import gameElements.*;
import gameElements.GameElement.RemStat;
import utils.*;
import java.util.*;
import java.util.Hashtable;

public class GameMap {
    private GameElement[][] map;
    private String srcPath;
    private int max_x, max_y; // max possible x and y in the whole map
    private boolean netMode;
    private Hashtable<Blks, BlkCoord> blkToPos = new Hashtable<>();
    private Hashtable<Integer, GameElement> idToEle = new Hashtable<>();

    private ArrayList<Tank> tks = new ArrayList<>();
    private ArrayList<KeyControllable> keyControllables = new ArrayList<>();

    public Hashtable<Blks, BlkCoord> getBlkToPos() {
        return blkToPos;
    }

    public void setBlkToPos(Hashtable<Blks, BlkCoord> blkToPos) {
        this.blkToPos = blkToPos;
    }

    public Hashtable<Integer, GameElement> getIdToEle() {
        return idToEle;
    }

    public void setIdToEle(Hashtable<Integer, GameElement> idToEle) {
        this.idToEle = idToEle;
    }

    public GameElement getEleById(int id) {
        return idToEle.get(id);
    }

    public ArrayList<Tank> getTks() {
        return tks;
    }

    public void setTks(ArrayList<Tank> tks) {
        this.tks = tks;
    }

    public GameMap(String mapName, boolean networkMode) {
        this.netMode = networkMode;
        this.srcPath = mapName;
        FileInputStream fin;
        InputStreamReader reader;
        BufferedReader buf_reader;
        try {
            fin = new FileInputStream(Consts.MAP_FOLDER + mapName);
            reader = new InputStreamReader(fin);
            buf_reader = new BufferedReader(reader);
            parseMap(buf_reader);
        } catch (Exception e) {
            System.out.println("excetion thrown when loading map: " + e.getMessage());
        }
    }

    public ArrayList<GameElement> getEles() {
        ArrayList<GameElement> res = new ArrayList<>();
        // move all game elements from idTOEle to res
        for (GameElement ele : idToEle.values()) {
            res.add(ele);
        }

        return res;
    }

    public GameElement getBlk(BlkCoord pos) {
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
        idToEle.remove(blk.getId());
        map[pos.x][pos.y] = null;
    }

    public boolean isNetMode() {
        return netMode;
    }

    public void setNetMode(boolean netMode) {
        this.netMode = netMode;
    }

    public ArrayList<KeyControllable> getKeyControllables() {
        return keyControllables;
    }

    public void setKeyControllables(ArrayList<KeyControllable> keyControllables) {
        this.keyControllables = keyControllables;
    }

    public void remTank(Tank tk) {
        tks.remove(tk);
        idToEle.remove(tk.getId());
    }

    public void remEle(GameElement ele) {
        if (ele instanceof Blks) {
            remBlk((Blks) ele);
        } else if (ele instanceof Tank) {
            remTank((Tank) ele);
        } else {
            idToEle.remove(ele.getId());
        }
        if (ele instanceof KeyControllable) {
            keyControllables.remove((KeyControllable)ele);
        }
    }

    public void remEle(int id){
        GameElement ele = idToEle.get(id);
        if(ele != null){
            remEle(ele);
        }
    }

    public void addEle(GameElement ele){
        if(ele instanceof Blks){
            Blks blk = (Blks) ele;
            blkToPos.put(blk, blk.getIntPos());
            map[blk.getIntPos().x][blk.getIntPos().y] = blk;
        }else if(ele instanceof Tank){
            tks.add((Tank) ele);
        }
        idToEle.put(ele.getId(), ele);
        if (ele instanceof KeyControllable) {
            keyControllables.add((KeyControllable) ele);
        }
    }

    public void addKeyControllable(KeyControllable kc){
        keyControllables.add(kc);
    }

    public void remKeyControllable(KeyControllable kc){
        keyControllables.remove(kc);
    }

    private void parseMap(BufferedReader bufr) {
        String curLine;
        try {
            // read the header
            String[] sizeStr = bufr.readLine().split("[*,\s]+", 0);
            max_x = Integer.parseInt(sizeStr[0]) + 2;
            max_y = Integer.parseInt(sizeStr[1]) + 2;
            map = new GameElement[max_x + 1][max_y + 1]; //
            ShVar.mapSize = new Coord(max_x, max_y);
            ShVar.map = this;
            for (int i = 0; i < max_x; i++) {
                map[i][0] = new SolidBlk(new BlkCoord(i, 0), ShVar.getNexId());
                map[i][max_y - 1] = new SolidBlk(new BlkCoord(i, max_y - 1), ShVar.getNexId());
                idToEle.put(map[i][0].getId(), map[i][0]);
                idToEle.put(map[i][max_y - 1].getId(), map[i][max_y - 1]);
                blkToPos.put((Blks) map[i][0], new BlkCoord(i, 0));
            }
            for (int i = 0; i < max_y; i++) {
                map[0][i] = new SolidBlk(new BlkCoord(0, i), ShVar.getNexId());
                map[max_x - 1][i] = new SolidBlk(new BlkCoord(max_x - 1, i), ShVar.getNexId());
                idToEle.put(map[0][i].getId(), map[0][i]);
                idToEle.put(map[max_x - 1][i].getId(), map[max_x - 1][i]);
                blkToPos.put((Blks) map[0][i], new BlkCoord(0, i));
            }

            int cur_y = 0;
            while ((curLine = bufr.readLine()) != null) {
                for (int cur_x = 0; cur_x < max_x - 2; cur_x++) {
                    switch (curLine.charAt(cur_x)) {
                        case 'O':
                            break;
                        case 'D':
                            map[cur_x + 1][cur_y + 1] = new DestrBlk(new BlkCoord(cur_x + 1, cur_y + 1), ShVar.getNexId());
                            // top left corner, so + 1 for y
                            break;
                        case 'S':
                            map[cur_x + 1][cur_y + 1] = new SolidBlk(new BlkCoord(cur_x + 1, cur_y + 1), ShVar.getNexId());
                            break;
                        case '1':
                            if (!netMode){
                                map[cur_x + 1][cur_y + 1] = new Tank(new Coord(cur_x + 1, cur_y + 1), Direct.UP, ShVar.getNexId());
                                tks.add((Tank) map[cur_x + 1][cur_y + 1]);
                                keyControllables.add((Tank)map[cur_x + 1][cur_y + 1]);
                            }
                            break;
                        case '2':
                            if (!netMode){
                                map[cur_x + 1][cur_y + 1] = new Tank(new Coord(cur_x + 1, cur_y + 1), Direct.DOWN, ShVar.getNexId());
                                tks.add((Tank) map[cur_x + 1][cur_y + 1]);
                                keyControllables.add((Tank)map[cur_x + 1][cur_y + 1]);
                            }
                            break;
                    }
                    if (map[cur_x + 1][cur_y + 1] != null)
                        idToEle.put(map[cur_x + 1][cur_y + 1].getId(), map[cur_x + 1][cur_y + 1]);
                    if (map[cur_x + 1][cur_y + 1] instanceof Blks)
                        blkToPos.put((Blks) map[cur_x + 1][cur_y + 1], new BlkCoord(cur_x + 1, cur_y + 1));
                }
                cur_y++;
            }
        } catch (Exception e) {
            System.out.println("exception thrown when parsing map: " + e.getMessage());
        }
    }

    public BlkCoord getRandPos() {
        int x = (int) (Math.random() * (max_x - 2)) + 1;
        int y = (int) (Math.random() * (max_y - 2)) + 1;
        return new BlkCoord(x, y);
    }

    public Tank addTankAtRandPos(int id) {
        BlkCoord tmpPos = null;
        while (getBlk((tmpPos = getRandPos())) != null) {
            // find a empty block
        } 
        Tank nt = new Tank(new Coord(tmpPos), Direct.getRandDir(), id);
        tks.add(nt);
        idToEle.put(id, nt);
        return nt;
    }

    public void setMap(GameElement[][] map) {
        this.map = map;
    }
}

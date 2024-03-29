package graphics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import gameElements.*;
import gameElements.GameElement.RemStat;
import utils.*;
import java.util.*;
import java.util.Hashtable;
import static utils.DbgPrinter.*;
import networkings.msgs.*;

public class GameMap {
    private GameElement[][] map;
    private String srcPath;
    private int max_x, max_y; // max possible x and y in the whole map
    private Hashtable<Blks, BlkCoord> blkToPos = new Hashtable<>();
    private Hashtable<Integer, GameElement> idToEle = new Hashtable<>();
    private ArrayList<Tank> tks = new ArrayList<>();

    public ArrayList<Bullet> getBlts() {
        return blts;
    }

    public void setBlts(ArrayList<Bullet> blts) {
        this.blts = blts;
    }

    public ArrayList<MovableElement> getMovables() {
        return movables;
    }

    public void setMovables(ArrayList<MovableElement> movables) {
        this.movables = movables;
    }

    private ArrayList<KeyCtrlable> keyCtrllables = new ArrayList<>();
    private ArrayList<Bullet> blts = new ArrayList<>();
    private ArrayList<MovableElement> movables = new ArrayList<>();
    private ArrayList<DbgBox> dbgBoxes = new ArrayList<>();

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
        } catch (Exception e) {
            dPrint("excetion thrown when loading map: " + e.getMessage());
        }
    }

    public GameMap(MapInitMsg msg){
        max_x = msg.getMax_x();
        max_y = msg.getMax_y();
        map = new GameElement[max_x + 1][max_y + 1];
        // generateSurroundingSolidBlk();
        for (int i = 0; i <= max_x; i++){
            for (int j = 0; j <= max_y; j++){
                switch (msg.getMpch()[i][j]){
                    case 'S':
                        addEle(new SolidBlk(new BlkCoord(i, j), msg.getMpid()[i][j]));
                        break;
                    case 'D':
                        addEle(new DestrBlk(new BlkCoord(i, j), msg.getMpid()[i][j]));
                        break;
                    default:
                }
            }
        }
        ShVar.map = this;
    }

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

    public ArrayList<GameElement> getEles() {
        ArrayList<GameElement> res = new ArrayList<>();
        // move all game elements from idTOEle to res
        res.addAll(idToEle.values());
        return res;
    }

    public GameElement getBlk(BlkCoord pos) {
        if (pos.x >= 0 && pos.x < max_x && pos.y >= 0 && pos.y < max_y)
            return map[pos.x][pos.y];
        dPrint("WARNING: getBlk out of bound");
        return null;
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

    public ArrayList<KeyCtrlable> getKeyCtrllables() {
        return keyCtrllables;
    }

    public void setKeyCtrllables(ArrayList<KeyCtrlable> keyControllables) {
        this.keyCtrllables = keyControllables;
    }

    public void remEle(GameElement ele) {
        if (ele instanceof Blks) {
            remBlk((Blks) ele);
        }
        if (ele instanceof Tank) {
            tks.remove((Tank) ele);
        }
        if (ele instanceof Bullet) {
            blts.remove(ele);
        }
        if (ele instanceof MovableElement) {
            movables.remove(ele);
        }
        if (ele instanceof KeyCtrlable) {
            keyCtrllables.remove((KeyCtrlable) ele);
        }

        if (ele instanceof DbgBox) {
            dbgBoxes.remove((DbgBox) ele);
        }

        idToEle.remove(ele.getId());
    }

    public void remByType(Class<?> cls) {
        ArrayList<GameElement> eles = getEles();
        for (GameElement ele : eles) {
            if (ele.getClass() == cls) {
                remEle(ele);
            }
        }
    }

    public ArrayList<DbgBox> getDbgBoxes() {
        return dbgBoxes;
    }

    public void setDbgBoxes(ArrayList<DbgBox> dbgBoxes) {
        this.dbgBoxes = dbgBoxes;
    }

    public void remEle(int id) {
        GameElement ele = idToEle.get(id);
        if (ele != null) {
            remEle(ele);
        }
    }

    public void addEle(GameElement ele) {
        assert !(ele instanceof BoundingBox) : "cannot add bounding box to map";
        if (Consts.IS_NET_MODE)
            dPrint((Consts.IS_SERVER ? "server" : "client") + " adding ele: " + ele.getId() + " "
                    + ele.getClass().getSimpleName());
        if (ele instanceof Blks) {
            Blks blk = (Blks) ele;
            blkToPos.put(blk, blk.getIntPos());
            map[blk.getIntPos().x][blk.getIntPos().y] = blk;
        }
        if (ele instanceof Tank) {
            for (Tank tk : tks) {
                ele.addNoHpChangeObj(tk.getId());
            }
            tks.add((Tank) ele);
        }
        if (ele instanceof Bullet) {
            blts.add((Bullet) ele);
        }
        if (ele instanceof MovableElement) {
            movables.add((MovableElement) ele);
        }

        if (ele instanceof DbgBox) {
            dbgBoxes.add((DbgBox) ele);
        }

        if (ele instanceof KeyCtrlable) {
            keyCtrllables.add((KeyCtrlable) ele);
        }

        idToEle.put(ele.getId(), ele);
    }

    public void addKeyControllable(KeyCtrlable kc) {
        keyCtrllables.add(kc);
    }

    public void remKeyControllable(KeyCtrlable kc) {
        keyCtrllables.remove(kc);
    }

    public BlkCoord getRandIntPos() {
        int x = (int) (Math.random() * (max_x - 2)) + 1;
        int y = (int) (Math.random() * (max_y - 2)) + 1;
        return new BlkCoord(x, y);
    }

    public Coord getRandPos() {
        float x = (float) (Math.random() * (max_x - 2)) + 1;
        float y = (float) (Math.random() * (max_y - 2)) + 1;
        return new Coord(x, y);
    }

    public boolean isIntersecting(GameElement ele) {
        for (GameElement other : getEles()) {
            if (ele.intersect(other))
                return true;
        }
        return false;
    }

    public Tank addTankAtRandPos(int id) {
        // should only be called by server
        Coord tmpPos = null;
        while (isIntersecting(new BoundingBox(tmpPos = getRandPos(), new Coord(1, 1)))) {
        }

        Tank nt = new Tank(tmpPos, Direct.getRandDir(), id, false);
        addEle(nt);
        return nt;
    }

    public void setMap(GameElement[][] map) {
        this.map = map;
    }


    private void generateSurroundingSolidBlk(){
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
    }

    static public char getEleCh(GameElement e){
        if (e instanceof DestrBlk){
            return 'D';
        } else if (e instanceof SolidBlk){
            return 'S';
        } else {
            return 'O';
        }
    }

    public char getEleCh(BlkCoord pos){
        return getEleCh(map[pos.x][pos.y]);
    }
    public char getEleCh(int x, int y){
        return getEleCh(map[x][y]);
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
            generateSurroundingSolidBlk();
            int cur_y = 0;
            while ((curLine = bufr.readLine()) != null) {
                for (int cur_x = 0; cur_x < max_x - 2; cur_x++) {
                    switch (curLine.charAt(cur_x)) {
                        case 'O':
                            break;
                        case 'D':
                            map[cur_x + 1][cur_y + 1] = new DestrBlk(new BlkCoord(cur_x + 1, cur_y + 1),
                                    ShVar.getNexId());
                            // top left corner, `so + 1 for y
                            break;
                        case 'S':
                            map[cur_x + 1][cur_y + 1] = new SolidBlk(new BlkCoord(cur_x + 1, cur_y + 1),
                                    ShVar.getNexId());
                            break;
                        case 'B':
                            // bot
                            if (!Consts.IS_NET_MODE) {
                                Tank tk;
                                addEle(tk = new BotTank(new Coord(cur_x + 1, cur_y + 1), Direct.UP, ShVar.getNexId(),
                                        false, 50, .25f));
                            }
                            break;

                        case '1':
                            if (!Consts.IS_NET_MODE) {
                                Tank tk;
                                addEle(tk = new Tank(new Coord(cur_x + 1, cur_y + 1), Direct.UP, ShVar.getNexId(),
                                        false));
                                keyCtrllables.add(tk);
                            }
                            break;
                        case '2':
                            if (!Consts.IS_NET_MODE) {
                                Tank tk;
                                addEle(tk = new Tank(new Coord(cur_x + 1, cur_y + 1), Direct.UP, ShVar.getNexId(),
                                        false));
                                keyCtrllables.add(tk);
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
            dPrint("exception thrown when parsing map: " + e.getMessage());
        }
    }
}

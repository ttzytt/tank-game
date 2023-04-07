package gameElements;

import utils.*;
import java.util.*;
import utils.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;
import javax.xml.validation.Validator;
import static utils.DbgPrinter.*;
import networkings.msgs.*;
import java.awt.*;
import java.awt.event.*;
import static utils.ShVar.*;

public class BotTank extends Tank {
    int pathFindFreq; // update the path after how many calls of mov(dt)
    int pathFindCnt; // count how many calls of mov(dt) since last update
    float pathsStep;
    Stack<Coord> path = new Stack<>();
    Tank tarTk;

    public BotTank(Coord _initPos, Direct _dir, int _id, boolean _updPosToServer, int _pathFindFreq, float _pathStep) {
        super(_initPos, _dir, _id, _updPosToServer);
        pathFindFreq = _pathFindFreq;
        pathFindCnt = pathFindFreq + 1;
        pathsStep = _pathStep;
        map.remKeyControllable(weapon);
        // weapon = null;
    }

    public BotTank(BornMsg bm, boolean updPosToServer) {
        super(bm, updPosToServer);
    }

    @Override
    public GeKeyCtrller getKeyController() {
        return null;
    }

    public void findTarget() {
        do {
            tarTk = map.getTks().get((int) (Math.random() * map.getTks().size()));
        } while (tarTk.getId() == id);
    }

    BoundingBox getFrtPathBox() {
        // return the bounding box that represents the path of bullet
        Coord bpos; // bullet come from the center of the tank
        bpos = new Coord(pos.x + size.x / 2, pos.y + size.y / 2);
        final float MXX = ShVar.mapSize.x + 5;
        final float MXY = ShVar.mapSize.y + 5;
        switch (dir) {
            case UP:
                return new BoundingBox(new Coord(bpos.x, 0),
                        new Coord(0, MXY - bpos.y));
            case DOWN:
                return new BoundingBox(new Coord(bpos.x, bpos.y), new Coord(0, MXY));
            case LEFT:
                return new BoundingBox(new Coord(0, bpos.y),
                        new Coord(MXX, 0));
            case RIGHT:
                return new BoundingBox(new Coord(bpos.x, bpos.y), new Coord(MXX, 0));
            default:
                return null;
        }
    }

    @Override
    public void mov(long dt) {
        if (pathFindCnt > pathFindFreq) {
            pathFindCnt = 0;
            if (!map.getTks().contains(tarTk)) {
                findTarget();
            }
            path = new PathFinder(pathsStep, this.getBox(), tarTk.getBox()).getPath();
        }
        pathFindCnt++;
        if (path.size() == 0)
            return;
        if (getFrtPathBox().intersect(tarTk.getBox())) {
            weapon.fire();
        }
        if (pos.dis_man(tarTk.getPos()) < 2)
            return;
        if (movToPos(path.peek(), dt)) {
            weapon.setPosByTkPos(pos);
            weapon.setCurDir(dir);
            path.pop();
        }
    }
}

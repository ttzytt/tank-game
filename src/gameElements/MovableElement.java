package gameElements;

import utils.*;
import java.awt.event.*;

import javax.swing.Timer;
import networkings.msgs.*;
import static utils.ShVar.*;

public abstract class MovableElement extends GameElement {
    Coord curVelo;
    Direct curDir;

    public MovableElement(int id) {
        super(id);
    }

    public void mov(long dt) {
        // dt is milliseconds
        // check if the point is in the map
        Coord npos = pos.add(curVelo.mul(dt / 1000f));
        if (npos.inRect(size, Coord.zero(), ShVar.mapSize))
            pos = npos;
    }

    public Coord getCurVelo() {
        return curVelo;
    }

    public void setCurVelo(Coord curVelo) {
        this.curVelo = curVelo;
    }

    public Direct getCurDir() {
        return curDir;
    }

    public void setCurDir(Direct curDir) {
        this.curDir = curDir;
    }

}

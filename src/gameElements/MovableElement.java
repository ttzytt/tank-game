package gameElements;

import utils.*;
import java.awt.event.*;

import javax.swing.Timer;
import networkings.msgs.*;
import static utils.ShVar.*;

public abstract class MovableElement extends GameElement {
    Coord curVelo = Coord.zero();
    Direct dir;
    float speed;
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

    public boolean movToPos(Coord tarPos, long dt){
        // move to the given position within the limit of speed
        // return if reached the target
        Coord nexPosDis = tarPos.sub(pos);
        float mxMovDis = speed * dt / 1000f;
        float actualMovDis = Math.min(nexPosDis.getDirSpeed(), mxMovDis);
        Coord npos = pos.add(nexPosDis.getUnit().mul(actualMovDis));
        setDir(nexPosDis.getDir());
        if (npos.inRect(size, Coord.zero(), ShVar.mapSize))
            pos = npos;
        return pos.equals(tarPos);
    }

    public Coord getCurVelo() {
        return curVelo;
    }

    public void setCurVelo(Coord curVelo) {
        this.curVelo = curVelo;
    }

    public Direct getDir() {
        return dir;
    }

    public void setDir(Direct curDir) {
        this.dir = curDir;
    }

    public BoundingBox getBox(){
        return new BoundingBox(this);
    }
}

package gameElements;

import utils.*;
import java.awt.event.*;

import javax.swing.Timer;
import networkings.msgs.*;
import static utils.ShVar.*;

public abstract class MovableElement extends GameElement {
    Coord curVelo;
    Direct curDir;
    boolean updPosToServer;
    Timer posUpdTimer;

    public MovableElement(int id, boolean updPosToServer) {
        super(id);
        this.updPosToServer = updPosToServer;
        if (updPosToServer) {
            posUpdTimer = new Timer(Consts.MOVABLE_POS_UPD_INTERV, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MovableUpdMsg mm = new MovableUpdMsg(MovableElement.this);
                    clntMsgToSend.add(mm);
                    // System.out.println(Class.class.getSimpleName() + id + " send msg " + mm);
                    synchronized (clntMsgToSend) {
                        clntMsgToSend.notify();
                    }
                }
            });
            posUpdTimer.start();
        }
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

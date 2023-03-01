package gameElements;

import utils.*;
import java.util.*;
import java.awt.event.*;
import static utils.Consts.UNDEF_ID;
import static utils.ShVar.*;
import networkings.msgs.*;
public abstract class Weapons implements KeyControllable{
    long lastFireTm;
    long fireInterv;
    float speed;
    Direct curDir = Direct.UP;
    int fromTankID;
    public long getLastFireTm() {
        return lastFireTm;
    }

    public void setLastFireTm(int lastFireTm) {
        this.lastFireTm = lastFireTm;
    }

    public long getFireInterv() {
        return fireInterv;
    }

    public void setFireInterv(int fireInterv) {
        this.fireInterv = fireInterv;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }


    public Direct getCurDir() {
        return curDir;
    }

    public void setCurDir(Direct curDir) {
        this.curDir = curDir;
    }

    Bullet Fire(boolean netMode) {
        long curTime = System.currentTimeMillis();
        if (curTime - lastFireTm >= fireInterv) {
            lastFireTm = curTime;
            Bullet bullet = new Bullet(pos, speed, curDir, getNexId(), fromTankID);
            bullet.addNoColObjs(fromTankID); 
            if (!netMode)
                map.addEle(bullet);
            else{
                clntMsgToSend.add(new BulletLaunchMsg(bullet));
            }
            return bullet;
        }
        return null;
    }

    Coord pos; 
    Coord size;
    Coord curVelo;

    public Weapons(Coord pos, float speed, int tankID) {
        fromTankID = tankID;
        // for odd tank id, use right shift
        // for even tank id use left shift
        this.speed = speed;
        this.pos = pos;
        this.size = new Coord(0, 0);
        curVelo = new Coord(0, 0);
    }

}

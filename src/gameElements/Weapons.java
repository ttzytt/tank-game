package gameElements;

import utils.*;
import java.util.*;
import java.awt.event.*;
import static utils.ShVar.*;
import networkings.msgs.*;
public abstract class Weapons implements KeyControllable{
    long lastFireTm;
    long fireInterv;
    float speed;
    Direct curDir = Direct.UP;
    int fromTankID;
    boolean updPosToServer;
    Class<Bullet> bulletType;

    public long getLastFireTm() {
        return lastFireTm;
    }

    public void setLastFireTm(long lastFireTm) {
        this.lastFireTm = lastFireTm;
    }

    public void setFireInterv(long fireInterv) {
        this.fireInterv = fireInterv;
    }

    public int getFromTankID() {
        return fromTankID;
    }

    public void setFromTankID(int fromTankID) {
        this.fromTankID = fromTankID;
    }

    public boolean isUpdPosToServer() {
        return updPosToServer;
    }

    public void setUpdPosToServer(boolean updPosToServer) {
        this.updPosToServer = updPosToServer;
    }

    public Coord getPos() {
        return pos;
    }

    public void setPos(Coord pos) {
        this.pos = pos;
    }

    public Coord getSize() {
        return size;
    }

    public void setSize(Coord size) {
        this.size = size;
    }

    public Coord getCurVelo() {
        return curVelo;
    }

    public void setCurVelo(Coord curVelo) {
        this.curVelo = curVelo;
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

    public void setPosByTkPos(Coord tkPos){
        if (curDir == Direct.UP || curDir == Direct.RIGHT)
            pos = tkPos.add(new Coord(curDir.turnRight(), Consts.TANK_OFFSET_TO_GUN))
                    .add(new Coord(curDir, .2f));
        else
            pos = tkPos.add(new Coord(curDir.turnLeft(), Consts.TANK_OFFSET_TO_GUN))
                    .add(new Coord(curDir, .2f));
    }


    Bullet Fire() {
        long curTime = System.currentTimeMillis();
        if (curTime - lastFireTm >= fireInterv) {
            lastFireTm = curTime;
            Bullet bullet = new AutoBullet(pos, speed, curDir, getNexId(), fromTankID, .3f);
            // Bullet bullet = new Bullet(pos, speed, curDir, getNexId(), fromTankID);
            bullet.addNoColObj(fromTankID);
            if (!updPosToServer)
                map.addEle(bullet);
            else{
                clntMsgToSend.add(new BulletLaunchMsg(bullet, fromTankID));
            }
            return bullet;
        }
        return null;
    }

    Coord pos; 
    Coord size;
    Coord curVelo;

    public Weapons(Coord pos, float speed, int tankID, boolean updPosToServer) {
        this.updPosToServer = updPosToServer;
        fromTankID = tankID;
        // for odd tank id, use right shift
        // for even tank id use left shift
        this.speed = speed;
        this.pos = pos;
        this.size = new Coord(0, 0);
        curVelo = new Coord(0, 0);
    }

}

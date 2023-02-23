package gameElements;

import utils.*;
import java.util.*;
import java.awt.event.*;

public abstract class Weapons extends MovableElement {
    long lastFireTm;
    long fireInterv;
    float speed;
    int side;
    Direct curDir = Direct.UP;
    ArrayList<GameElement> inFlightBullets = new ArrayList<>();
    @Override
    public ArrayList<GameElement> getDirectSubEle(){
        inFlightBullets.removeIf(nullEle -> nullEle.removeStat == RemoveStat.REMED);
        return inFlightBullets;
    }

    @Override
    public void mov(long dt) {
        super.mov(dt);
    }

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

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public Direct getCurDir() {
        return curDir;
    }

    public void setCurDir(Direct curDir) {
        this.curDir = curDir;
    }

    Bullet Fire() {
        long curTime = System.currentTimeMillis();
        if (curTime - lastFireTm >= fireInterv) {
            lastFireTm = curTime;
            Bullet bullet = new Bullet(pos, speed, curDir);
            inFlightBullets.add(bullet);
            return bullet;
        }
        return null;
    }

    public ArrayList<GameElement> getInFlightBullets() {
        return inFlightBullets;
    }

    public void setInFlightBullets(ArrayList<GameElement> inFlightBullets) {
        this.inFlightBullets = inFlightBullets;
    }

    public Weapons(Coord pos, float speed, int id) {
        this.side = id;
        this.speed = speed;
        this.pos = pos;
        this.size = new Coord(0, 0);
        curVelo = new Coord(0, 0);
    }

}

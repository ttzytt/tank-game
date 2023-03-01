package gameElements;

import utils.*;
import java.util.*;
import javax.swing.*;
import graphics.GameMap;
import networkings.msgs.HPUpdMsg;

import java.awt.*;

abstract public class GameElement {
    public static class TankBlkCollisionAuxInfo {
        boolean isTankEnclosed; // is the collided object completely enclosed by this object (the blk)
        boolean isTankOffsetLowerOrLeft; // if it is not fully enclosed, then is the offset lower or left
        boolean isTankOffsetHigherOrRight; // if it is not fully enclosed, then is the offset higher or right
        boolean isHor;
        GameElement lowerOpp, midOpp, upperOpp; // the lower, middle, upper opposite side of the blk

        TankBlkCollisionAuxInfo(Blks b, Tank t) {
            Direct curDir = t.curDir;
            // if the tank is moving horizontally, check if its y1 y2 is in the range of the
            // block
            // if the tank is moving vertically, check if its x1 x2 is in the range of the
            // block
            isHor = curDir == Direct.LEFT || curDir == Direct.RIGHT;
            float x1 = b.getIntPos().x, x2 = b.getIntPos().x + b.getSize().x;
            float y1 = b.getIntPos().y, y2 = b.getIntPos().y + b.getSize().y;
            float tx1 = t.pos.x, tx2 = t.pos.x + t.size.x;
            float ty1 = t.pos.y, ty2 = t.pos.y + t.size.y;
            isTankEnclosed = isHor ? (y1 <= ty1 && ty2 <= y2) : (x1 <= tx1 && tx2 <= x2);

            // is the tank lower than (x1 or y1) or higher than (x2 or y2)
            isTankOffsetLowerOrLeft = isHor ? (ty2 > y2) : (tx1 < x1);
            isTankOffsetHigherOrRight = isHor ? (ty2 < y2) : (tx1 > x1);
            // if it is lower, and the back of the lower block have no wall, move back
            // same worked for the upper block
            lowerOpp = ShVar.map
                    .getBlk(b.getIntPos().getAdj(curDir.turnLeft(), 1).getAdj(curDir.turnOpp(), 2));
            midOpp = ShVar.map.getBlk(b.getIntPos().getAdj(curDir.turnOpp(), 2));
            upperOpp = ShVar.map
                    .getBlk(b.getIntPos().getAdj(curDir.turnRight(), 1).getAdj(curDir.turnOpp(), 2));
            if (curDir == Direct.DOWN || curDir == Direct.RIGHT) {
                GameElement tmp = lowerOpp;
                lowerOpp = upperOpp;
                upperOpp = tmp;
            }
        }

        public boolean isTankEnclosed() {
            return isTankEnclosed;
        }

        public void setTankEnclosed(boolean isTankEnclosed) {
            this.isTankEnclosed = isTankEnclosed;
        }

        public boolean isTankOffsetLowerOrLeft() {
            return isTankOffsetLowerOrLeft;
        }

        public void setTankOffsetLowerOrLeft(boolean isTankOffsetLowerOrLeft) {
            this.isTankOffsetLowerOrLeft = isTankOffsetLowerOrLeft;
        }

        public boolean isTankOffsetHigherOrRight() {
            return isTankOffsetHigherOrRight;
        }

        public void setTankOffsetHigherOrRight(boolean isTankOffsetHigherOrRight) {
            this.isTankOffsetHigherOrRight = isTankOffsetHigherOrRight;
        }

        public boolean isHor() {
            return isHor;
        }

        public void setHor(boolean isHor) {
            this.isHor = isHor;
        }

        public GameElement getLowerOpp() {
            return lowerOpp;
        }

        public void setLowerOpp(GameElement lowerOpp) {
            this.lowerOpp = lowerOpp;
        }

        public GameElement getMidOpp() {
            return midOpp;
        }

        public void setMidOpp(GameElement midOpp) {
            this.midOpp = midOpp;
        }

        public GameElement getUpperOpp() {
            return upperOpp;
        }

        public void setUpperOpp(GameElement upperOpp) {
            this.upperOpp = upperOpp;
        }
    }

    public enum RemStat {
        NOT_REM, TO_REM, REMED;
    }

    public static boolean processCollision(GameElement a, GameElement b) {
        if (a == b)
            return false;
        if (a.getNoColObjs().contains(b.getId()) || b.getNoColObjs().contains(a.getId()))
            return false;
        if (a.getSize().equals(Coord.zero()) || b.getSize().equals(Coord.zero()))
            return false;
        if (a.getRemoveStat() != RemStat.NOT_REM || b.getRemoveStat() != RemStat.NOT_REM)
            return false;
        if (a.getHp() != -1) {
            a.processCollision(b);
        }
        if (b.getHp() != -1) {
            b.processCollision(a);
        }
        if (a instanceof Bullet)
            a.setRemStat(RemStat.TO_REM);
        if (b instanceof Bullet)
            b.setRemStat(RemStat.TO_REM);
        return true;
    }

    ArrayList<Integer> noColObjs = new ArrayList<Integer>(); // the objects that should not be collided with (stored the
                                                             // id)

    GameElement lowerOpp, midOpp, upperOpp; // the lower, middle, upper opposite side of the blk

    boolean HPchanged = false;

    ImagePanel img;

    Coord pos = new Coord(0, 0);
    Coord size = new Coord(0, 0);

    int hp, damage;

    public int id;

    public RemStat removeStat = RemStat.NOT_REM;

    public GameElement(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getNoColObjs() {
        return noColObjs;
    }

    public void setNoColObjs(ArrayList<Integer> noColObjs) {
        this.noColObjs = noColObjs;
    }

    public void addNoColObjs(int noColObj) {
        this.noColObjs.add(noColObj);
    }

    public GameElement getLowerOpp() {
        return lowerOpp;
    }

    public void setLowerOpp(GameElement lowerOpp) {
        this.lowerOpp = lowerOpp;
    }

    public GameElement getMidOpp() {
        return midOpp;
    }

    public void setMidOpp(GameElement midOpp) {
        this.midOpp = midOpp;
    }

    public GameElement getUpperOpp() {
        return upperOpp;
    }

    public void setUpperOpp(GameElement upperOpp) {
        this.upperOpp = upperOpp;
    }

    public boolean isHPchanged() {
        return HPchanged;
    };

    public void setHPchanged(boolean hPchanged) {
        HPchanged = hPchanged;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean intersect(GameElement other) {
        // from pos and size, calculate if the rectangle collide with other
        // a is this rect, b is other rect
        float ax1 = pos.x, ax2 = pos.x + size.x, ay1 = pos.y, ay2 = pos.y + size.y;
        float bx1 = other.pos.x, bx2 = other.pos.x + other.size.x, by1 = other.pos.y, by2 = other.pos.y + other.size.y;
        return ax1 <= bx2 && ax2 >= bx1 && ay1 <= by2 && ay2 >= by1 ||
                bx1 <= ax2 && bx2 >= ax1 && by1 <= ay2 && by2 >= ay1;
    }

    public RemStat getRemoveStat() {
        return removeStat;
    }

    public void setRemStat(RemStat removeStat) {
        this.removeStat = removeStat;
    }

    public void setImgBound(int pixPerBlk) {
        if (img != null)
            img.setBounds(Coord.toRect(pos.mul(pixPerBlk), size.mul(pixPerBlk)));
    }

    public ImagePanel getImg() {
        return img;
    }

    public void setImg(ImagePanel img) {
        this.img = img;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    protected boolean processCollision(GameElement other) {

        hp -= other.getDamage();
        if (other.getDamage() > 0)
            HPchanged = true;
        if (hp <= 0) {
            removeStat = RemStat.TO_REM;
            hp = -2;
        }
        return true;
    }
}

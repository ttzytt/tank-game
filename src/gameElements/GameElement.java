package gameElements;

import utils.*;
import java.util.*;
import javax.swing.*;
import graphics.GameMap;
import java.awt.*;

abstract public class GameElement {
    ImagePanel img;
    Coord pos = new Coord(0, 0);
    Coord size = new Coord(0, 0);
    int hp, damage;

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
    }

    public enum RemoveStat {
        NOT_REM, TO_REM, REMED;
    };

    public static boolean processCollision(GameElement a, GameElement b) {
        if (a == b)
            return false;
        if (a.getAllSubEle().contains(b) || b.getAllSubEle().contains(a))
            return false;
        if (a.getRemoveStat() != RemoveStat.NOT_REM || b.getRemoveStat() != RemoveStat.NOT_REM)
            return false;
        if (a.getHp() != -1) {
            a.processCollision(b);
        }
        if (b.getHp() != -1) {
            b.processCollision(a);
        }
        if (a instanceof Bullet)
            a.setRemoveStat(RemoveStat.TO_REM);
        if (b instanceof Bullet)
            b.setRemoveStat(RemoveStat.TO_REM);
        return true;
    }

    protected boolean processCollision(GameElement other) {

        hp -= other.getDamage();
        if (hp <= 0) {
            removeStat = RemoveStat.TO_REM;
            hp = -2;
        }
        return true;
    }

    public RemoveStat removeStat = RemoveStat.NOT_REM;

    public boolean intersect(GameElement other) {
        // from pos and size, calculate if the rectangle collide with other
        // a is this rect, b is other rect
        float ax1 = pos.x, ax2 = pos.x + size.x, ay1 = pos.y, ay2 = pos.y + size.y;
        float bx1 = other.pos.x, bx2 = other.pos.x + other.size.x, by1 = other.pos.y, by2 = other.pos.y + other.size.y;
        return ax1 <= bx2 && ax2 >= bx1 && ay1 <= by2 && ay2 >= by1 ||
                bx1 <= ax2 && bx2 >= ax1 && by1 <= ay2 && by2 >= ay1;
    }

    public RemoveStat getRemoveStat() {
        return removeStat;
    }

    public void setRemoveStat(RemoveStat removeStat) {
        this.removeStat = removeStat;
    }

    public ArrayList<GameElement> getDirectSubEle() {
        return null;
    }

    public ArrayList<GameElement> getAllSubEle() {
        ArrayList<GameElement> res = new ArrayList<>();
        ArrayList<GameElement> subEle = getDirectSubEle();
        if (subEle != null) {
            res.addAll(subEle);
            for (GameElement ele : subEle) {
                res.addAll(ele.getAllSubEle());
            }
        }
        return res;
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
}

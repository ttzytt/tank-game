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
    GameMap map;
    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public enum RemoveStat {
        NOT_REM, TO_REM, REMED;
    };

    public static boolean processCollision(GameElement a, GameElement b) {
        if (a == b) return false;
        if (a.getAllSubEle().contains(b) || b.getAllSubEle().contains(a)) return false;
        if (a.getRemoveStat() != RemoveStat.NOT_REM || b.getRemoveStat() != RemoveStat.NOT_REM) return false;
        if (a.getHp() != -1){
            a.processCollision(b);
        }
        if (b.getHp() != -1){
            b.processCollision(a);
        }
        if (a instanceof Bullet) a.setRemoveStat(RemoveStat.TO_REM);
        if (b instanceof Bullet) b.setRemoveStat(RemoveStat.TO_REM);
        return true;
    }

    protected boolean processCollision(GameElement other) {

        hp -= other.getDamage();
        System.out.println("Collision detected: " + this + "(" + getHp() + ") " + other + "(" + other.getHp() + ")" );
        if (hp <= 0) {
            System.out.println(this + " is destroyed.");
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

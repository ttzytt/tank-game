package gameElements;

import utils.*;
import java.util.*;
import utils.*;
import javax.swing.*;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;
import javax.xml.validation.Validator;

import java.awt.*;
import java.awt.event.*;

public class Tank extends MovableElement {
    int id; // id = 1, use direction keys, id = 2, use WASD keys
    float speed = Consts.INIT_TANK_SPEED;
    public MoveHandler moveHandler = new MoveHandler();
    ImagePanel iup, idn, ilf, irt;
    Weapons weapon;

    public Tank(Coord initPos, int id) {
        hp = Consts.INIT_TANK_HP;
        weapon = new Gun(initPos, id);
        curDir = Direct.UP;
        size = Helpers.getAspRate(Consts.T1_UP_IMG).mul(Consts.TANK_SIZE_RATIO);
        curVelo = new Coord(0, 0);
        damage = Consts.INIT_TANK_DAMAGE;
        pos = initPos;
        iup = new ImagePanel(id == 1 ? Consts.T1_UP_IMG_PATH : Consts.T2_UP_IMG_PATH);
        img = iup.deepCopy();
        idn = new ImagePanel(id == 1 ? Consts.T1_DOWN_IMG_PATH : Consts.T2_DOWN_IMG_PATH);
        ilf = new ImagePanel(id == 1 ? Consts.T1_LEFT_IMG_PATH : Consts.T2_LEFT_IMG_PATH);
        irt = new ImagePanel(id == 1 ? Consts.T1_RIGHT_IMG_PATH : Consts.T2_RIGHT_IMG_PATH);
        this.id = id;
    }

    @Override
    public MoveHandler getMoveHandler() {
        return moveHandler;
    }

    public void setMoveHandler(MoveHandler moveHandler) {
        this.moveHandler = moveHandler;
    }

    @Override
    public ArrayList<GameElement> getDirectSubEle() {
        ArrayList<GameElement> res = new ArrayList<GameElement>();
        res.add(weapon);
        return res;
    }

    public int dirToKeyCode(Direct dir) {
        if (id == 1) {
            if (dir == Direct.UP)
                return KeyEvent.VK_UP;
            else if (dir == Direct.DOWN)
                return KeyEvent.VK_DOWN;
            else if (dir == Direct.LEFT)
                return KeyEvent.VK_LEFT;
            else if (dir == Direct.RIGHT)
                return KeyEvent.VK_RIGHT;
        } else if (id == 2) {
            if (dir == Direct.UP)
                return KeyEvent.VK_W;
            else if (dir == Direct.DOWN)
                return KeyEvent.VK_S;
            else if (dir == Direct.LEFT)
                return KeyEvent.VK_A;
            else if (dir == Direct.RIGHT)
                return KeyEvent.VK_D;
        }
        return -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    protected boolean processCollision(GameElement other) {
        super.processCollision(other);
        // move back
        if (other instanceof Blks) {
            // also ensure that the other direction have no wall
            Blks b = (Blks) other;
            TankBlkCollisionAuxInfo auxInfo = new TankBlkCollisionAuxInfo(b, this);
            if (!(auxInfo.midOpp instanceof Blks) && 
                auxInfo.isTankEnclosed || 
                (auxInfo.isTankOffsetLowerOrLeft && !(auxInfo.lowerOpp instanceof Blks) && !(auxInfo.midOpp instanceof Blks)) || 
                (auxInfo.isTankOffsetHigherOrRight && !(auxInfo.upperOpp instanceof Blks) && !(auxInfo.midOpp instanceof Blks))
            ) {
                pos = pos.sub(new Coord(curDir, Consts.TANK_COLLIDE_BOUNCE_DIST));
                // tank can bounce bank if and only if it will not bounce to another block
            } 
            else
                pos = pos.sub(new Coord(curDir, 0.05f));
            
        }
        return true;
    }

    // key event listener
    public class MoveHandler implements KeyListener {
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == dirToKeyCode(Direct.UP)) {
                weapon.curVelo = curVelo = new Coord(Direct.UP, speed);
                img.updImg(iup.getBufImg());
                weapon.curDir = curDir = Direct.UP;
                size = Helpers.getAspRate(Consts.T1_UP_IMG).mul(Consts.TANK_SIZE_RATIO);
            } else if (keyCode == dirToKeyCode(Direct.DOWN)) {
                weapon.curVelo = curVelo = new Coord(Direct.DOWN, speed);
                img.updImg(idn.getBufImg());
                weapon.curDir = curDir = Direct.DOWN;
                size = Helpers.getAspRate(Consts.T1_DOWN_IMG).mul(Consts.TANK_SIZE_RATIO);
            } else if (keyCode == dirToKeyCode(Direct.LEFT)) {
                weapon.curVelo = curVelo = new Coord(Direct.LEFT, speed);
                img.updImg(ilf.getBufImg());
                weapon.curDir = curDir = Direct.LEFT;
                size = Helpers.getAspRate(Consts.T1_LEFT_IMG).mul(Consts.TANK_SIZE_RATIO);
            } else if (keyCode == dirToKeyCode(Direct.RIGHT)) {
                weapon.curVelo = curVelo = new Coord(Direct.RIGHT, speed);
                img.updImg(irt.getBufImg());
                weapon.curDir = curDir = Direct.RIGHT;
                size = Helpers.getAspRate(Consts.T1_RIGHT_IMG).mul(Consts.TANK_SIZE_RATIO);
            }
            if (curDir == Direct.UP || curDir == Direct.RIGHT)
                weapon.pos = pos.add(new Coord(curDir.turnRight(), Consts.TANK_OFFSET_TO_GUN))
                        .add(new Coord(curDir, .2f));
            else
                weapon.pos = pos.add(new Coord(curDir.turnLeft(), Consts.TANK_OFFSET_TO_GUN))
                        .add(new Coord(curDir, .2f));
            Helpers.validateAndRepaint(img);
        }

        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == dirToKeyCode(Direct.UP) || keyCode == dirToKeyCode(Direct.DOWN)
                    || keyCode == dirToKeyCode(Direct.LEFT) || keyCode == dirToKeyCode(Direct.RIGHT)) {
                curVelo = new Coord(0, 0);
            }
        }

        public void keyTyped(KeyEvent e) {
        }
    }
}

package gameElements;

import utils.*;
import java.util.*;
import utils.*;
import javax.swing.*;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;
import javax.xml.validation.Validator;

import networkings.msgs.*;
import java.awt.*;
import java.awt.event.*;
import static utils.ShVar.*;

public class Tank extends MovableElement implements KeyControllable {
    float speed = Consts.INIT_TANK_SPEED;

    public ImagePanel getIup() {
        return iup;
    }

    public void setIup(ImagePanel iup) {
        this.iup = iup;
    }

    public ImagePanel getIdn() {
        return idn;
    }

    public void setIdn(ImagePanel idn) {
        this.idn = idn;
    }

    public ImagePanel getIlf() {
        return ilf;
    }

    public void setIlf(ImagePanel ilf) {
        this.ilf = ilf;
    }

    public ImagePanel getIrt() {
        return irt;
    }

    public void setIrt(ImagePanel irt) {
        this.irt = irt;
    }

    public Weapons getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapons weapon) {
        this.weapon = weapon;
    }

    public MoveHandler keyController = new MoveHandler(false);
    public MoveHandler remoteModeKeyController = new MoveHandler(true);
    ImagePanel iup, idn, ilf, irt;
    Weapons weapon;

    public Tank(Coord initPos, Direct dir, int id) {
        super(id);
        hp = Consts.INIT_TANK_HP;
        weapon = new Gun(initPos, id);
        map.addKeyControllable(weapon);
        curDir = Direct.UP;
        size = Helpers.getAspRate(Consts.T1_UP_IMG).mul(Consts.TANK_SIZE_RATIO);
        curVelo = new Coord(0, 0);
        damage = Consts.INIT_TANK_DAMAGE;
        pos = initPos;
        iup = new ImagePanel(id == 1 ? Consts.T1_UP_IMG_PATH : Consts.T2_UP_IMG_PATH);
        idn = new ImagePanel(id == 1 ? Consts.T1_DOWN_IMG_PATH : Consts.T2_DOWN_IMG_PATH);
        ilf = new ImagePanel(id == 1 ? Consts.T1_LEFT_IMG_PATH : Consts.T2_LEFT_IMG_PATH);
        irt = new ImagePanel(id == 1 ? Consts.T1_RIGHT_IMG_PATH : Consts.T2_RIGHT_IMG_PATH);
        this.id = id;
        if (dir == Direct.UP)
            img = iup.deepCopy();
        else if (dir == Direct.DOWN)
            img = idn.deepCopy();
        else if (dir == Direct.LEFT)
            img = ilf.deepCopy();
        else if (dir == Direct.RIGHT)
            img = irt.deepCopy();
        this.curDir = dir;
    }

    @Override
    public MoveHandler getNetModeKeyController() {
        return remoteModeKeyController;
    }

    public void setRemoteModeKeyController(MoveHandler remoteModeKeyController) {
        this.remoteModeKeyController = remoteModeKeyController;
    }

    public Tank(BornMsg bm) {
        this(bm.pos, bm.dir, bm.id);
    }

    @Override
    public MoveHandler getKeyController() {
        return keyController;
    }

    public void setKeyController(MoveHandler moveHandler) {
        this.keyController = moveHandler;
    }

    public int dirToKeyCode(Direct dir) {
        if (id % 2 == 1) {
            if (dir == Direct.UP)
                return KeyEvent.VK_UP;
            else if (dir == Direct.DOWN)
                return KeyEvent.VK_DOWN;
            else if (dir == Direct.LEFT)
                return KeyEvent.VK_LEFT;
            else if (dir == Direct.RIGHT)
                return KeyEvent.VK_RIGHT;
        } else if (id % 2 == 0) {
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
                    (auxInfo.isTankOffsetLowerOrLeft && !(auxInfo.lowerOpp instanceof Blks)
                            && !(auxInfo.midOpp instanceof Blks))
                    ||
                    (auxInfo.isTankOffsetHigherOrRight && !(auxInfo.upperOpp instanceof Blks)
                            && !(auxInfo.midOpp instanceof Blks))) {
                pos = pos.sub(new Coord(curDir, Consts.TANK_COLLIDE_BOUNCE_DIST));
                // tank can bounce bank if and only if it will not bounce to another block
            } else
                pos = pos.sub(new Coord(curDir, 0.05f));

        }
        return true;
    }

    // key event listener
    public class MoveHandler implements KeyListener {
        boolean netMode;

        MoveHandler(boolean netWorkMode) {
            this.netMode = netWorkMode;
        }

        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == dirToKeyCode(Direct.UP)) {
                weapon.curVelo = curVelo = new Coord(Direct.UP, speed);
                weapon.curDir = curDir = Direct.UP;
                size = Helpers.getAspRate(Consts.T1_UP_IMG).mul(Consts.TANK_SIZE_RATIO);
                img.updImg(iup.getBufImg());
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
            if (netMode) {
                MovableUpdMsg mm = new MovableUpdMsg(Tank.this);
                clntMsgToSend.add(mm);
                clntMsgToSend.notify();
            }
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

package gameElements;

import utils.*;
import java.util.*;
import utils.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;
import javax.xml.validation.Validator;

import networkings.msgs.*;
import java.awt.*;
import java.awt.event.*;
import static utils.ShVar.*;

public class Tank extends KeyControlMovable {
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

    public MoveHandler keyController = new MoveHandler();
    ImagePanel iup, idn, ilf, irt;
    Weapons weapon;

    public Tank(Coord _initPos, Direct _dir, int _id, boolean _updPosToServer) {
        super(_id, _updPosToServer);
        hp = Consts.INIT_TANK_HP;
        weapon = new Gun(_initPos, _id, _updPosToServer);
        map.addKeyControllable(weapon);
        this.dir = Direct.UP;
        size = Helpers.getAspRate(Consts.T1_UP_IMG).mul(Consts.TANK_SIZE_RATIO);
        curVelo = new Coord(0, 0);
        damage = Consts.INIT_TANK_DAMAGE;
        pos = _initPos;
        iup = new ImagePanel(_id == 1 ? Consts.T1_UP_IMG_PATH : Consts.T2_UP_IMG_PATH);
        idn = new ImagePanel(_id == 1 ? Consts.T1_DOWN_IMG_PATH : Consts.T2_DOWN_IMG_PATH);
        ilf = new ImagePanel(_id == 1 ? Consts.T1_LEFT_IMG_PATH : Consts.T2_LEFT_IMG_PATH);
        irt = new ImagePanel(_id == 1 ? Consts.T1_RIGHT_IMG_PATH : Consts.T2_RIGHT_IMG_PATH);
        this.id = _id;
        if (_dir == Direct.UP)
            img = iup.deepCopy();
        else if (_dir == Direct.DOWN)
            img = idn.deepCopy();
        else if (_dir == Direct.LEFT)
            img = ilf.deepCopy();
        else if (_dir == Direct.RIGHT)
            img = irt.deepCopy();
        this.dir = _dir;
        weapon.setCurDir(_dir);
    }

    public Tank(BornMsg bm, boolean updPosToServer) {
        this(bm.pos, bm.dir, bm.id, updPosToServer);
    }

    @Override
    public MoveHandler getKeyController() {
        if (updPosToServer || ! Consts.IS_NET_MODE)
            return keyController;
        else
            return null;
    }

    public void setKeyController(MoveHandler moveHandler) {
        this.keyController = moveHandler;
    }

    public int dirToKeyCode(Direct _dir) {
        if (id % 2 == 1) {
            if (_dir == Direct.UP)
                return KeyEvent.VK_UP;
            else if (_dir == Direct.DOWN)
                return KeyEvent.VK_DOWN;
            else if (_dir == Direct.LEFT)
                return KeyEvent.VK_LEFT;
            else if (_dir == Direct.RIGHT)
                return KeyEvent.VK_RIGHT;
        } else if (id % 2 == 0) {
            if (_dir == Direct.UP)
                return KeyEvent.VK_W;
            else if (_dir == Direct.DOWN)
                return KeyEvent.VK_S;
            else if (_dir == Direct.LEFT)
                return KeyEvent.VK_A;
            else if (_dir == Direct.RIGHT)
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
    public void setPos(Coord pos){
        super.setPos(pos);
        weapon.setPosByTkPos(pos);
    }

    @Override
    public void setDir(Direct dir) {
        // weapon.setCurDir(dir);
        switch (dir) {
            case UP:
                img.updImg(iup.getBufImg());
                size = Helpers.getAspRate(Consts.T1_UP_IMG).mul(Consts.TANK_SIZE_RATIO);
                break;
            case DOWN:
                img.updImg(idn.getBufImg());
                size = Helpers.getAspRate(Consts.T1_DOWN_IMG).mul(Consts.TANK_SIZE_RATIO);
                break;
            case LEFT:
                img.updImg(ilf.getBufImg());
                size = Helpers.getAspRate(Consts.T1_LEFT_IMG).mul(Consts.TANK_SIZE_RATIO);
                break;
            case RIGHT:
                img.updImg(irt.getBufImg());
                size = Helpers.getAspRate(Consts.T1_RIGHT_IMG).mul(Consts.TANK_SIZE_RATIO);
                break;
        }
        if (Consts.IS_NET_MODE && !Consts.IS_SERVER)
        img.setDbgL("" + getId());
        this.dir = dir;
        weapon.setCurDir(dir);
    }

    @Override
    protected boolean processCollision(GameElement other) {
        super.processCollision(other);
        // move back
        if (other instanceof Blks) {
            // also ensure that the other direction have no wall
            Blks b = (Blks) other;
            TankBlkCollisionAuxInfo auxInfo = new TankBlkCollisionAuxInfo(b, this);
            
            float nearTkAtBack = Float.MAX_VALUE / 2;

            for (Tank tks : map.getTks()){
                // check the nereast tank at the back of this tank
                // TODO
            }

            if (!(auxInfo.midOpp instanceof Blks) &&
                    auxInfo.isTankEnclosed ||
                    (auxInfo.isTankOffsetLowerOrLeft && !(auxInfo.lowerOpp instanceof Blks)
                            && !(auxInfo.midOpp instanceof Blks))
                    ||
                    (auxInfo.isTankOffsetHigherOrRight && !(auxInfo.upperOpp instanceof Blks)
                            && !(auxInfo.midOpp instanceof Blks))) {
                pos = pos.sub(new Coord(dir, Consts.TANK_COLLIDE_BOUNCE_DIST));
                // tank can bounce bank if and only if it will not bounce to another block
            } else
                pos = pos.sub(new Coord(dir, 0.05f));

        }
        return true;
    }

    // key event listener
    public class MoveHandler implements KeyListener {

        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == dirToKeyCode(Direct.UP)) {
                weapon.curVelo = curVelo = new Coord(Direct.UP, speed);
                setDir(Direct.UP);
            } else if (keyCode == dirToKeyCode(Direct.DOWN)) {
                weapon.curVelo = curVelo = new Coord(Direct.DOWN, speed);
                setDir(Direct.DOWN);
            } else if (keyCode == dirToKeyCode(Direct.LEFT)) {
                weapon.curVelo = curVelo = new Coord(Direct.LEFT, speed);
                setDir(Direct.LEFT);
            } else if (keyCode == dirToKeyCode(Direct.RIGHT)) {
                weapon.curVelo = curVelo = new Coord(Direct.RIGHT, speed);
                setDir(Direct.RIGHT);
        }
            weapon.setPosByTkPos(pos);
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

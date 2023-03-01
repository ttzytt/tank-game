package gameElements;

import utils.*;
import java.awt.event.*;

public class Gun extends Weapons  {
    boolean useLeftShift;
    public Gun(Coord pos, int fromTankID) {
        super(pos, Consts.INIT_BULLET_SPEED, fromTankID);
        fireInterv = Consts.INIT_BULLET_FIRE_INTERV;
        useLeftShift = fromTankID % 2 == 0; // for even tank id, use left shift
        lastFireTm = 0;
    }
    KeyListener keyController = new MoveHandler(false);
    KeyListener remoteModeKeyController = new MoveHandler(true);

    public void setKeyController(KeyListener keyController) {
        this.keyController = keyController;
    }

    @Override
    public KeyListener getNetModeKeyController() {
        return remoteModeKeyController;
    }

    public void setRemoteModeKeyController(KeyListener remoteModeKeyController) {
        this.remoteModeKeyController = remoteModeKeyController;
    }

    @Override
    public KeyListener getKeyController() {
        return keyController;
    }

    public class MoveHandler implements KeyListener {
        boolean netMode;
        public MoveHandler(boolean netWorkMode) {
            this.netMode = netWorkMode;
        }        
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // shift to fire, left shift for side == 1, right shift for side == 2
            if (e.getKeyCode() != KeyEvent.VK_SHIFT)
                return;
            if (!useLeftShift && e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT)
                Fire(netMode);
            else if (useLeftShift && e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT)
                Fire(netMode);
        }
        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}

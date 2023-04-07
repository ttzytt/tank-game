package gameElements;

import utils.*;
import java.awt.event.*;

public class Gun extends Weapons  {
    boolean useLeftShift;
    public Gun(Coord pos, int fromTankID, boolean updPosToServer) {
        super(pos, Consts.INIT_BULLET_SPEED, fromTankID, updPosToServer);
        fireInterv = Consts.INIT_BULLET_FIRE_INTERV;
        useLeftShift = fromTankID % 2 == 0; // for even tank id, use left shift
        lastFireTm = 0;
    }
    KeyListener keyController = new MoveHandler();

    public void setKeyController(KeyListener keyController) {
        this.keyController = keyController;
    }


    @Override
    public GeKeyCtrller getKeyController() {
        return new GeKeyCtrller(fromTankID, keyController);
    }

    public class MoveHandler implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // shift to fire, left shift for side == 1, right shift for side == 2
            if (e.getKeyCode() != KeyEvent.VK_SHIFT)
                return;
            if (!useLeftShift && e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT)
                fire();
            else if (useLeftShift && e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT)
                fire();
        }
        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}

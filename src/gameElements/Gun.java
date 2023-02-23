package gameElements;

import utils.*;
import java.awt.event.*;

public class Gun extends Weapons {

    public Gun(Coord pos, int side) {
        super(pos, Consts.INIT_BULLET_SPEED, side);
        fireInterv = Consts.INIT_BULLET_FIRE_INTERV;
        lastFireTm = 0;
        hp = -1;
    }
    KeyListener moveHandler = new MoveHandler();

    @Override
    public KeyListener getMoveHandler() {
        return moveHandler;
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
            if (side == 1 && e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT)
                Fire();
            else if (side == 2 && e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT)
                Fire();
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}

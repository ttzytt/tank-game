package gameElements;
import utils.*;
import java.awt.event.*;

import javax.swing.Timer;
import networkings.msgs.*;
import static utils.ShVar.*;

public abstract class KeyControlMovable extends MovableElement implements KeyControllable{
    // only both movable and controlable elements need to send update information actively to the server
    // movable elements like bullet do not need to actively update the position
    // the server will update this information
    
    // controllable object like Weapon controlls the firing of bullet
    // so also do not need to actively update the position  

    Timer posUpdTimer;
    boolean updPosToServer;
    public KeyControlMovable(int id, boolean updPosToServer){
        super(id);
        this.updPosToServer = updPosToServer;
        if (updPosToServer) {
            posUpdTimer = new Timer(Consts.MOVABLE_POS_UPD_INTERV, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MovableUpdMsg mm = new MovableUpdMsg(KeyControlMovable.this);
                    clntMsgToSend.add(mm);
                    synchronized (clntMsgToSend) {
                        clntMsgToSend.notify();
                    }
                }
            });
            posUpdTimer.start();
        }
    }

    @Override
    public void setUpdPosToServer(boolean updPosToServer) {
        this.updPosToServer = updPosToServer;
    }

}
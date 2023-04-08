package graphics;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import networkings.msgs.MapInitMsg;
import utils.*;

public class GUI {
    GamePanel gpanel;
    JFrame frame;
    public GUI(GamePanel gpanle) {
        this.gpanel = gpanle;
        try {
            Thread.sleep(200); // wait to get uid
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String uid = "" + (Consts.IS_NET_MODE ? Consts.CLNT_UID : "");
        frame = new JFrame("Tank Game(uid=" + uid + ")");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setResizable(true);
        frame.add(gpanle);
        // for (KeyListener kl : gpanle.getKeyListeners())
        //     frame.addKeyListener(kl);
        frame.setVisible(true);
    }

    public GUI(){
        this(new GamePanel());
    }

    public GUI(MapInitMsg msg){
        this(new GamePanel(msg));
    }

    public GamePanel getGpanel() {
        return gpanel;
    }

    public void setGpanel(GamePanel gpanle) {
        this.gpanel = gpanle;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
}

package graphics;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import utils.*;

public class GUI {
    GamePanel gpanle;
    JFrame frame;
    public GUI() {
        gpanle = new GamePanel();
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

    public GamePanel getGpanle() {
        return gpanle;
    }

    public void setGpanle(GamePanel gpanle) {
        this.gpanle = gpanle;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
}

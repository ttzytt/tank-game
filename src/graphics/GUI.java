package graphics;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import utils.*;

public class GUI {
    GamePanel gpanle;
    JFrame frame;
    public GUI() {
        frame = new JFrame("Tank Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setResizable(true);
        gpanle = new GamePanel();
        frame.add(gpanle);
        for (KeyListener kl : gpanle.getKeyListeners())
            frame.addKeyListener(kl);
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

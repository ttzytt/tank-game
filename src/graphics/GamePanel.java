package graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import utils.*;
import utils.Helpers.*;
import gameElements.*;
import gameElements.GameElement.RemStat;
import networkings.msgs.MapInitMsg;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.SwingUtilities;
import static utils.DbgPrinter.*;

public class GamePanel extends JPanel {
    private GameMap map;
    private long lstRefresh = -1;
    private Timer timer = new Timer(Consts.FRAME_INTERV, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Helpers.validateAndRepaint(GamePanel.this);

        }
    });
    private Set<ImagePanel> addedImg = ConcurrentHashMap.newKeySet();
    private Set<KeyCtrlable> addedKeyControllable = ConcurrentHashMap.newKeySet();

    public int calcBlkPixSiz() {
        // calculate the size of each block in pixels
        float fwid = (float) getWidth();
        float fhei = (float) getHeight();

        float bwid = fwid / (float) map.getMax_x();
        float bhei = fhei / (float) map.getMax_y();

        // use the limiting one
        return (int) Math.min(bwid, bhei);
        // floor the value
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // clear all components

        // move the thing that need to
        ArrayList<GameElement> eles = map.getEles();
        if (lstRefresh != -1) {
            long inter = System.currentTimeMillis() - lstRefresh;
            for (GameElement a : eles) {
                if (a.getRemoveStat() == RemStat.TO_REM && a.getImg() != null) {
                    remove(a.getImg());
                
                    addedImg.remove(a.getImg());
                    a.setRemStat(RemStat.REMED);
                    map.remEle(a);
                    continue;
                }
                if (a.getImg() != null && !addedImg.contains(a.getImg()) && a.getRemoveStat() == RemStat.NOT_REM) {
                    add(a.getImg());
                    addedImg.add(a.getImg());
                }

                if (!Consts.IS_NET_MODE && a instanceof MovableElement) {
                    boolean intersected = false;
                    MovableElement ma = (MovableElement) a;
                    BoundingBox box = new BoundingBox(ma);
                    box.mov(inter * 2);
                    for (GameElement b : eles) {
                        if (b instanceof DbgBox) continue;
                        if (box.intersect(b)) {
                            intersected |= GameElement.processCollision(a, b);
                        }
                    }
                    if (!intersected)
                        ((MovableElement) a).mov(inter);
                }
                Helpers.validateAndRepaint(a.getImg());
            }

            for (KeyCtrlable kc : map.getKeyCtrllables()) {
                if (!addedKeyControllable.contains(kc)) {
                    if (kc.getKeyController() == null
                            || (Consts.IS_NET_MODE && kc.getKeyController().getCtrlId() != Consts.CLNT_UID))
                        continue;
                    addKeyListener(kc.getKeyController().getListener());
                    addedKeyControllable.add(kc);
                    setFocusable(true);
                    requestFocusInWindow();
                    dPrint("Added key controller " + kc);
                }
            }
        }

        // fill it with black background
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, map.getMax_x() * calcBlkPixSiz(), map.getMax_y() * calcBlkPixSiz());
        for (GameElement a : eles) {
            a.setImgBound(calcBlkPixSiz()); // reset image size according the new pixel size
            Helpers.validateAndRepaint(a.getImg());
        }
        lstRefresh = System.currentTimeMillis();
    }

    public GamePanel(GameMap map) {
        ShVar.gp = this;
        timer.start();
        this.map = map;
        lstRefresh = System.currentTimeMillis();
        setLayout(null);
        for (GameElement b : map.getEles()) {
            if (b.getImg() != null) {
                add(b.getImg());
                addedImg.add(b.getImg());
            }
        }

        for (KeyCtrlable kc : map.getKeyCtrllables()) {
            if (kc.getKeyController() == null || (Consts.IS_NET_MODE && kc.getKeyController().getCtrlId() != Consts.CLNT_UID))
                continue;
            addKeyListener(kc.getKeyController().getListener());
            setFocusable(true);
            requestFocusInWindow();
            dPrint("Added key controller " + kc);
            addedKeyControllable.add(kc);
        }
    }

    public GamePanel() {
        this(new GameMap("default.txt"));
    }

    public GamePanel(MapInitMsg msg) {
        this(new GameMap(msg));
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(map.getMax_x() * calcBlkPixSiz(), map.getMax_y() * calcBlkPixSiz());
    }
}

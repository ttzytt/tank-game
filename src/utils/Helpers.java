package utils;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.*;

public class Helpers {
    public static void validateAndRepaint(Component comp) {
        if (comp == null)
            return;
        Stack<Component> fas = new Stack<Component>();
        do {
            fas.push(comp);
        } while ((comp = comp.getParent()) != null);
        while (!fas.empty()) {
            Component cur = fas.pop();
            cur.repaint();
            cur.validate();
        }
    }

    public static Coord getAspRate(ImagePanel ip) {
        // the max side is 1, the other side is the ratio
        int w = ip.getOwid(), h = ip.getOhei();
        if (w == 0 || h == 0) return new Coord(0, 0);
        if (w > h) return new Coord(1, (float) h / w);
        else return new Coord((float) w / h, 1);
    }

}
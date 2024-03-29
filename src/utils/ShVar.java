package utils;

import graphics.*;
import networkings.msgs.EvtMsg;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;

// shared variables
public class ShVar {
    public static Coord mapSize;
    public static GameMap map;
    public static GamePanel gp;
    public static Queue<EvtMsg> clntMsgToSend = new ConcurrentLinkedQueue<>();
    private static int nexId = 0;
    synchronized public static int getNexId(){
        return nexId++;
    }
    synchronized public static int getCurId(){
        assert nexId > 0 : "getNexId have not been called before";
        return nexId - 1;
    }
}

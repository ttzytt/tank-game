package networkings;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Queue;
import static utils.ShVar.*;
import graphics.*;
import networkings.msgs.*;
import utils.Consts;
import static utils.DbgPrinter.*;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.plaf.synth.SynthSeparatorUI;

import gameElements.*;
import gameElements.GameElement.RemStat;

public class ServMain {

    Queue<EvtMsg> msgRecved = new PriorityBlockingQueue<>();
    // min heap, to process the msg that arrives earlier
    Queue<EvtMsg> msgToBroadCast = new PriorityBlockingQueue<>();
    long lstRefresh = -1;

    public class Clnt {
        public int tankID;
        public int UDPport;
        public String IP;
        public Queue<EvtMsg> msgToSend = new ConcurrentLinkedQueue<>();
        public InetSocketAddress getAddr() {
            return new InetSocketAddress(IP, UDPport);
        }

        public Clnt(int tankID, int UDPport, String IP) {
            this.tankID = tankID;
            this.UDPport = UDPport;
            this.IP = IP;
        }

        long lstActive = System.currentTimeMillis();

        public void recvClntMsg() {
            while (true) {
                EvtMsg newM = (EvtMsg) udpSock.recvObj(getAddr());
                lstActive = System.currentTimeMillis();
                msgRecved.add(newM);
                synchronized (msgRecved) {
                    msgRecved.notify();
                }
            }
        }

        public void invokeRecvMsg() {
            // need to open thread for each of the client
            // to ensure the msg is received on time
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    recvClntMsg();
                }
            });
            t.start();
        }
    }

    public void sendClntMsg() {
        while (true) {
            // check if others called notify
            while (msgToBroadCast.isEmpty()) {
                // untile there are something to send
                synchronized (msgToBroadCast) {
                    try {
                        msgToBroadCast.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                        dPrint("client msg wait failed");
                    }
                }
            }
            while (msgToBroadCast.size() > 0) {
                EvtMsg curM = msgToBroadCast.poll();
                for (Clnt c : clnts) {
                    udpSock.sendObj(curM, c.getAddr());
                }
            }
            for (Clnt c : clnts){
                synchronized (c.msgToSend) {
                    while (c.msgToSend.size() > 0) {
                        udpSock.sendObj(c.msgToSend.poll(), c.getAddr());
                    }
                }
            }
        }
    }

    public void invokeSendMsg() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                sendClntMsg();
            }
        });
        t.start();
    }

    ArrayList<Clnt> clnts = new ArrayList<>();
    GameMap mp = new GameMap("withoutTank.txt");

    public void acceptClnt() {
        while (true) {
            Socket cSock = null;
            try {
                cSock = listenSock.accept();
                DataInputStream dis = new DataInputStream(cSock.getInputStream());
                int cUDPport = dis.readInt();
                dPrint("one client connected port = " + cUDPport);
                int tkId;
                Clnt clnt = new Clnt(tkId = getNexId(), cUDPport, cSock.getInetAddress().getHostAddress());
                clnts.add(clnt);
                clnt.invokeRecvMsg();
                DataOutputStream dos = new DataOutputStream(cSock.getOutputStream());
                dos.writeInt(tkId);
                dos.writeInt(ServConsts.UDP_PORT);
                mp.addTankAtRandPos(tkId);
                dPrint("MapInitMsg sent to " + clnt.getAddr());
                clnt.msgToSend.add(new MapInitMsg(mp));
                // tell the client about all the exisiting palyers
                for (Tank tk : mp.getTks()) {
                    msgToBroadCast.add(new BornMsg((Tank) tk, 0));
                }

                synchronized (msgToBroadCast) {
                    msgToBroadCast.notify();
                }
            } catch (Exception e) {
                e.printStackTrace();
                dPrint("client connection failed");
            } finally {
                try {
                    if (cSock != null)
                        cSock.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void invokeAcceptClient() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                acceptClnt();
            }
        });
        t.start();
    }

    boolean processCollision(MovableElement ele, long inter) {
        boolean collided = false;
        BoundingBox box = new BoundingBox(ele);
        // box.mov(inter);
        for (GameElement other : mp. getEles()) {
            if (box.intersect(other)) {
                collided |= GameElement.processCollision(ele, other);
                if (GameElement.processCollision(ele, other)){
                    dPrint(ele + " collide with " + other);
                }
                if (other.isHpChanged()) {
                    msgToBroadCast.add(new HPUpdMsg(other));
                    other.setHpChanged(false);
                    synchronized (msgToBroadCast) {
                        msgToBroadCast.notify();
                    }
                }
            }
        }
        if (ele.isHpChanged()) {
            msgToBroadCast.add(new HPUpdMsg(ele));
            synchronized (msgToBroadCast) {
                msgToBroadCast.notify();
            }
            ele.setHpChanged(false);
        }
        return collided;
    }

    public ServMain() {
        Consts.IS_SERVER = true;
        try {
            listenSock = new ServerSocket(ServConsts.TCP_PORT);
            udpSock = new UDPwrap(ServConsts.UDP_PORT);
            dPrint("server socket created");
        } catch (Exception e) {
            e.printStackTrace();
            dPrint("server socket creation failed");
        }
        invokeAcceptClient();
        invokeSendMsg();

        Timer procTimer = new Timer(ServConsts.PROC_INTERV, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (msgRecved) {
                    // prevent continuous increase of msgRecved
                    // between each process, need to wait for some time
                    // because it is possible to first receive move msg then create msg
                    while (msgRecved.size() > 0) {
                        EvtMsg m = msgRecved.poll();
                        // dPrint("cur evt= " + m);
                        MovableElement me = null;
                        if (m instanceof MovableUpdMsg) {
                            MovableUpdMsg mmsg = (MovableUpdMsg) m;
                            me = (MovableElement) mp.getEleById(mmsg.id);
                            if (me == null) continue;
                            me.setCurVelo(mmsg.getVelo());
                            if (mmsg.getDir() != null)
                            me.setDir(mmsg.getDir());
                            // dPrint(mmsg + " type= " + me);
                        } else if (m instanceof BulletLaunchMsg) {
                            BulletLaunchMsg bmsg = (BulletLaunchMsg) m;
                            bmsg.id = getNexId(); // assign id to the bullet
                            mp.addEle(me = new Bullet(bmsg));
                            if (me == null) continue;
                            bmsg.setPrio(0); // need to create something before update them
                            msgToBroadCast.add(bmsg);
                            synchronized (msgToBroadCast) {
                                msgToBroadCast.notify();
                            }
                        } else {
                            throw new RuntimeException("only two types of msg are allowed by client");
                        }
                    }
                }

                ArrayList<GameElement> eles = mp.getEles();
                // dPrint("start processcollision");
                for (GameElement a : eles) {

                    long interval = lstRefresh == -1 ? 0 : System.currentTimeMillis() - lstRefresh;
                    if (a.getRemoveStat() == RemStat.TO_REM) {
                        // dPrint("serv broadcasted rem msg for id = " + a.getId() + " type = "
                        //         + a.getClass().getSimpleName());
                        msgToBroadCast.add(new RemEleMsg(a));
                        synchronized (msgToBroadCast) {
                            msgToBroadCast.notify();
                        }
                        mp.remEle(a);
                    }
                    if (a instanceof MovableElement) {
                        MovableElement ma = (MovableElement) a;
                        if (!processCollision(ma, interval)) {
                            ma.mov(interval);
                            msgToBroadCast.add(new MovableUpdMsg(ma));
                            synchronized (msgToBroadCast) {
                                msgToBroadCast.notify();
                            }
                        }
                    }
                }
                lstRefresh = System.currentTimeMillis();
            }
        });
        procTimer.start();

    }

    public static void main(String[] args) {
        ServMain s = new ServMain();
    }

    private ServerSocket listenSock;
    private UDPwrap udpSock;
}

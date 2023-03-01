package networkings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import static utils.ShVar.*;
import graphics.*;
import networkings.msgs.*;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

import gameElements.*;
import gameElements.GameElement.RemStat;

public class ServMain {

    Queue<EvtMsg> msgRecved = new PriorityBlockingQueue<EvtMsg>();
    // min heap, to process the msg that arrives earlier
    Queue<EvtMsg> msgToBroadCast = new ConcurrentLinkedQueue<>();
    long lstRefresh = -1;

    public class Clnt {
        public int tankID;
        public int UDPport;
        public String IP;

        public InetSocketAddress getAddr(){
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
                EvtMsg newM = (EvtMsg)udpSock.recvObj(getAddr());
                lstActive = System.currentTimeMillis();
                msgRecved.add(newM);
                msgRecved.notify();
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
                try {
                    msgToBroadCast.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("client msg wait failed");
                }
            }
            while (msgToBroadCast.size() > 0) {
                EvtMsg curM = msgToBroadCast.poll();
                for (Clnt c : clnts) {
                    udpSock.sendObj(curM, c.getAddr());
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
    GameMap mp = new GameMap("withoutTank.txt", true);
    public void acceptClnt() {
        while (true) {
            Socket cSock = null;
            try {
                cSock = listenSock.accept();
                System.out.println("one client connected");
                DataInputStream dis = new DataInputStream(cSock.getInputStream());
                int cUDPport = dis.readInt();
                Clnt clnt = new Clnt(getNexId(), cUDPport, cSock.getInetAddress().getHostAddress());
                clnts.add(clnt);
                clnt.invokeRecvMsg();
                DataOutputStream dos = new DataOutputStream(cSock.getOutputStream());
                dos.writeInt(getCurId());
                dos.writeInt(ServConsts.UDP_PORT);
                mp.addTankAtRandPos(getCurId());
                msgToBroadCast.add(new BornMsg((Tank) mp.getEleById(getCurId())));
                msgToBroadCast.notify();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("client connection failed");
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


    boolean processCollision(GameElement ele){
        boolean collided = false;
        for (GameElement other : mp.getEles()){
            if (ele.intersect(other)){
                collided |= GameElement.processCollision(ele, other);
                if (other.isHPchanged()){
                    msgToBroadCast.add(new HPUpdMsg(other));
                    other.setHPchanged(false);
                    msgToBroadCast.notify();
                }
            }
        }
        if (ele.isHPchanged()){
            msgToBroadCast.add(new HPUpdMsg(ele));
            msgToBroadCast.notify();
            ele.setHPchanged(false);
        }
        return collided;
    }

    public ServMain() {
        try {
            listenSock = new ServerSocket(ServConsts.TCP_PORT);
            udpSock = new UDPwrap(ServConsts.UDP_PORT);
            System.out.println("server socket created");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("server socket creation failed");
        }
        invokeSendMsg();
        invokeAcceptClient();

        while (true) {
            // game loop
            // update position of players and create new bullets
            synchronized (msgRecved) {
                // prevent continuous increase of msgRecved
                while (msgRecved.size() > 0) {
                    EvtMsg m = msgRecved.poll();
                    MovableElement me = null;
                    if (m instanceof MovableUpdMsg) {
                        MovableUpdMsg mmsg = (MovableUpdMsg) m;
                        me = (MovableElement) mp.getEleById(mmsg.id);
                        me.setCurVelo(mmsg.velo);
                    } else if (m instanceof BulletLaunchMsg) {
                        BulletLaunchMsg bmsg = (BulletLaunchMsg) m;
                        mp.addEle(me = new Bullet(bmsg));
                        bmsg.id = getNexId(); // assign id to the bullet
                        msgToBroadCast.add(bmsg);
                        msgToBroadCast.notify();
                    } else {
                        throw new RuntimeException("only two types of msg are allowed by client");
                    }
                    long interval = lstRefresh == -1 ? 0 : System.currentTimeMillis() - lstRefresh;
                    lstRefresh = System.currentTimeMillis();
                    if (!processCollision(me)) {
                        me.mov(interval);
                        msgToBroadCast.add(new MovableUpdMsg(me));
                        msgToBroadCast.notify();
                    }
                }
            }
            for (GameElement a : mp.getEles()){
                if (a.getRemoveStat() == RemStat.TO_REM){
                    msgToBroadCast.add(new RemEleMsg(a));
                    msgToBroadCast.notify();
                    mp.remEle(a);
                }
            }
        }
        
    }

    public static void main(String[] args) {
        ServMain s = new ServMain();
    }

    private ServerSocket listenSock;
    private UDPwrap udpSock;
}

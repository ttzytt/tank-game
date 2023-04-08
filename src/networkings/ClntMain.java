package networkings;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;
import static utils.DbgPrinter.*;
import javax.sql.rowset.spi.SyncResolver;

import gameElements.*;
import gameElements.GameElement.RemStat;
import utils.Consts;
import static utils.ShVar.*;
import graphics.*;
import networkings.msgs.*;

public class ClntMain {
    Queue<EvtMsg> msgRecved = new PriorityBlockingQueue<EvtMsg>();
    InetSocketAddress servUdpAddr, servIPAddr;
    UDPwrap udpSock;
    int uid; // user id
    GUI gui;

    public void recvServMsg() {
        while (true) {
            EvtMsg newM = (EvtMsg) udpSock.recvObj(servUdpAddr);
            msgRecved.add(newM);
            synchronized (msgRecved) {
                msgRecved.notify();
            }
        }
    }

    public void invokeRecvServMsg() {
        Thread t = new Thread() {
            public void run() {
                recvServMsg();
            }
        };
        t.start();
    }

    public void sendServMsg() {
        while (true) {
            if (clntMsgToSend.isEmpty()) {
                try {
                    synchronized (clntMsgToSend) {
                        clntMsgToSend.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            EvtMsg m = clntMsgToSend.poll();
            udpSock.sendObj(m, servUdpAddr);
        }
    }

    public void invokeSendServMsg() {
        Thread t = new Thread() {
            public void run() {
                sendServMsg();
            }
        };
        t.start();
    }

    public int getRandPort() {
        // from 6000 to 65535
        return (int) (Math.random() * 5535 + 6000);
    }

    public void connect() {
        Socket sock = null;
        try {
            sock = new Socket(Consts.SERV_IP, ServConsts.TCP_PORT);
            udpSock = new UDPwrap(getRandPort());
            DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
            dPrint("port= " + udpSock.getUdpSock().getLocalPort());
            dos.writeInt(udpSock.getUdpSock().getLocalPort()); // send the serve the UDP port
            DataInputStream dis = new DataInputStream(sock.getInputStream());
            uid = dis.readInt();
            int servUDPport = dis.readInt();
            servUdpAddr = new InetSocketAddress(Consts.SERV_IP, servUDPport);
            dPrint("connected to the server, uid=" + uid);
        } catch (Exception e) {
            dPrint("failed to connect to the server");
            e.printStackTrace();
        } finally {
            try {
                if (sock != null)
                    sock.close();
            } catch (Exception e) {
                dPrint("failed to close socket");
                e.printStackTrace();
            }
        }
        Consts.CLNT_UID = uid;
    }

    public ClntMain() {
        Consts.IS_SERVER = false;
        connect();

        invokeRecvServMsg();
        invokeSendServMsg();
        
        // wait for mapinit msg to be received
        while(msgRecved.isEmpty()){
        }
        while(!(msgRecved.peek() instanceof MapInitMsg)){
        }
        MapInitMsg mim = (MapInitMsg) msgRecved.poll();
        gui = new GUI(mim);

        while (true) {
            // process the received events
            // synchronized (msgRecved)
            while (msgRecved.size() > 0) {
                EvtMsg m = msgRecved.poll();
                if (m instanceof BornMsg) {
                    BornMsg bm = (BornMsg) m;
                    if (map.getEleById(bm.id) != null) // already added this tank
                        continue;
                    map.addEle(new Tank(bm, uid == bm.getId()));
                    // update only when the tank is equal to uid
                } else if (m instanceof BulletLaunchMsg) {
                    BulletLaunchMsg bm = (BulletLaunchMsg) m;
                    map.addEle(new Bullet(bm));
                } else if (m instanceof HPUpdMsg) {
                    HPUpdMsg hm = (HPUpdMsg) m;
                    GameElement ge = map.getEleById(hm.getId());
                    if (ge == null)
                        continue;
                    ge.setHp(hm.getNewHp());
                } else if (m instanceof MovableUpdMsg) {
                    MovableUpdMsg mm = (MovableUpdMsg) m;
                    MovableElement me = (MovableElement) map.getEleById(mm.getId());
                    if (me == null)
                        continue;
                    me.setPos(mm.getPos());
                    if (me.getId() != uid)
                        me.setDir(mm.getDir());
                    // no need to set direction for self
                } else if (m instanceof RemEleMsg) {
                    RemEleMsg rm = (RemEleMsg) m;
                    GameElement ge = map.getEleById(rm.getId());
                    if (ge == null)
                        continue;
                    ge.setRemStat(RemStat.TO_REM);
                }
            }
        }
        // }
    }

    public static void main(String[] args) {
        new ClntMain();
    }
}

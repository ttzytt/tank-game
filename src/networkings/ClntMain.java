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
    GUI gui = new GUI(true);
    public void recvServMsg() {
        while (true) {
            EvtMsg newM = (EvtMsg) udpSock.recvObj(servUdpAddr);
            msgRecved.add(newM);
            msgRecved.notify();
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
                    clntMsgToSend.wait();
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
            dos.writeInt(udpSock.getUdpSock().getPort()); // send the serve the UDP port
            DataInputStream dis = new DataInputStream(sock.getInputStream());
            uid = dis.readInt();
            System.out.println("connected to the server");
        } catch (Exception e) {
            System.out.println("failed to connect to the server");
            e.printStackTrace();
        } finally {
            try {
                if (sock != null)
                    sock.close();
            } catch (Exception e) {
                System.out.println("failed to close socket");
                e.printStackTrace();
            }
        }

    }

    public ClntMain() {
        connect();
        invokeRecvServMsg();
        invokeSendServMsg();
        while(true){
            // process the received events
            synchronized (msgRecved){
                while(msgRecved.size() > 0){
                    EvtMsg m = msgRecved.poll();
                    if (m instanceof BornMsg){
                        BornMsg bm = (BornMsg) m;
                        map.addEle(new Tank(bm));
                    } else if (m instanceof BulletLaunchMsg){
                        BulletLaunchMsg bm = (BulletLaunchMsg) m;
                        map.addEle(new Bullet(bm));
                    } else if (m instanceof HPUpdMsg){
                        HPUpdMsg hm = (HPUpdMsg) m;
                        map.getEleById(hm.getId()).setHp(hm.getNewHp());
                    } else if (m instanceof MovableUpdMsg){
                        MovableUpdMsg mm = (MovableUpdMsg) m;
                        map.getEleById(mm.getId()).setPos(mm.getPos());
                    } else if (m instanceof RemEleMsg){
                        RemEleMsg rm = (RemEleMsg) m;
                        map.getEleById(rm.getId()).setRemStat(RemStat.TO_REM);
                    }
                }
            }
        }
    }
}

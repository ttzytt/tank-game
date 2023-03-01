package networkings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UDPwrap {
    void sendObj(Object obj, InetSocketAddress addr){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();
            byte[] serilized = baos.toByteArray();
            DatagramPacket dp = new DatagramPacket(serilized, serilized.length);
            dp.setSocketAddress(addr);
            udpSock.send(dp);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("client msg send failed");
        }
    }

    Object recvObj(InetSocketAddress addr){
        try {
            byte[] buf = new byte[ServConsts.UDP_BUF_SZ];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            dp.setSocketAddress(addr);
            udpSock.receive(dp);
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("client msg recv failed");
        }
        return null;
    }

    public DatagramSocket getUdpSock() {
        return udpSock;
    }

    public void setUdpSock(DatagramSocket udpSock) {
        this.udpSock = udpSock;
    }

    public UDPwrap(DatagramSocket udpSock){
        this.udpSock = udpSock;
    }

    public UDPwrap(int port){
        try {
            this.udpSock = new DatagramSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("udp socket init failed");
        }
    }

    public DatagramSocket udpSock;
}

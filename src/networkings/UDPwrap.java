package networkings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import static utils.DbgPrinter.*;
public class UDPwrap {
    void sendObj(Object obj, InetSocketAddress addr){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            byte[] serilized = baos.toByteArray();
            dPrint("obj: " + obj + " ser len " + serilized.length);
            DatagramPacket dp = new DatagramPacket(serilized, serilized.length);
            dp.setSocketAddress(addr);
            udpSock.send(dp);
        }  catch (Exception e) {
            e.printStackTrace();
            dPrint("client msg send failed");
        }
    }

    Object recvObj(InetSocketAddress addr){
        try {
            byte[] buf = new byte[ServConsts.UDP_BUF_SZ];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            dp.setSocketAddress(addr);
            udpSock.receive(dp);
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            // dPrint("len= " + dp.getLength() + " off= " + dp.getOffset());
            ObjectInputStream ois = new ObjectInputStream(bais);
            ois.close();
            return ois.readObject();
        } catch (EOFException ee) {
            dPrint("read EOF, retrying");
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                dPrint("sleep failed");
            }
            recvObj(addr);
        }catch (Exception e) {
            e.printStackTrace();
            dPrint("client msg recv failed");
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
            dPrint("udp socket init failed");
        }
    }

    public DatagramSocket udpSock;
}

package gameElements;
import java.awt.event.KeyListener;

public interface KeyCtrlable {
    public GeKeyCtrller getKeyController();
    // TODO: need also return the id that the controller is controlling
    public void setUpdPosToServer(boolean updPosToServer);
}

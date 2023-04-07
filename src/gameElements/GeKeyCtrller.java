package gameElements;
import java.awt.event.KeyListener;


public class GeKeyCtrller {
    int ctrlId;
    // for weapons' keylistener, it is controlling the tank
    KeyListener listener;
    public GeKeyCtrller(int ctrlId, KeyListener listener) {
        this.ctrlId = ctrlId;
        this.listener = listener;
    }
    public int getCtrlId() {
        return ctrlId;
    }
    public void setCtrlId(int ctrlId) {
        this.ctrlId = ctrlId;
    }
    public KeyListener getListener() {
        return listener;
    }
    public void setListener(KeyListener listener) {
        this.listener = listener;
    }

}

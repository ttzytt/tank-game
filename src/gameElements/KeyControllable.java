package gameElements;
import java.awt.event.KeyListener;

public interface KeyControllable {
    public KeyListener getKeyController();
    public void setUpdPosToServer(boolean updPosToServer);
}

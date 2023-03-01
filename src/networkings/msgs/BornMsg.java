package networkings.msgs;
import utils.*;
import gameElements.Tank;
public class BornMsg extends EvtMsg {
    public int id;
    public Coord pos;
    public Direct dir;
    public BornMsg(int id, Coord pos, Direct dir){
        super();
        this.id = id;
        this.pos = pos;
        this.dir = dir;
    }
    public BornMsg(Tank tk){
        super();
        this.id = tk.getId();
        this.pos = tk.getPos();
        this.dir = tk.getCurDir();
    }
}

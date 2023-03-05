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

    public BornMsg(int id, Coord pos, Direct dir, long prio){
        this(id, pos, dir);
        this.prio = prio;
    }

    public BornMsg(Tank tk){
        super();
        this.id = tk.getId();
        this.pos = tk.getPos();
        this.dir = tk.getCurDir();
    }

    public BornMsg(Tank tk, long prio) {
        this(tk);
        this.prio = prio;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Coord getPos() {
        return pos;
    }
    public void setPos(Coord pos) {
        this.pos = pos;
    }
    public Direct getDir() {
        return dir;
    }
    public void setDir(Direct dir) {
        this.dir = dir;
    }

    @Override
    public String toString(){
        return "BornMsg: id: " + id + " pos: " + pos + " dir: " + dir;
    }

}

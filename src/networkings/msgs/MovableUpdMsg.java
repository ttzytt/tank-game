package networkings.msgs;
import gameElements.MovableElement;
import utils.*;

public class MovableUpdMsg extends EvtMsg {
    public int id;
    public Coord pos, velo;
    public Direct dir;
    // server will send the client pos
    // client will send the server velocity
    public MovableUpdMsg(int movableId, Coord pos, Coord velo, Direct dir){
        super();
        this.id = movableId;
        this.pos = pos;
        this.velo = velo;
    }

    public MovableUpdMsg(int movableId, Coord pos, Coord velo, Direct dir, long prio){
        this(movableId, pos, velo, dir);
        this.prio = prio;
    }

    public MovableUpdMsg(MovableElement me){
        this(me.getId(), me.getPos(), me.getCurVelo(), me.getDir());
    }

    public MovableUpdMsg(MovableElement me, long prio) {
        this(me.getId(), me.getPos(), me.getCurVelo(), me.getDir(), prio);
    }

    public int getId() {
        return id;
    }
    public void setId(int movableId) {
    this.id = movableId;
    }
    public Coord getPos() {
        return pos;
    }
    public void setPos(Coord pos) {
        this.pos = pos;
    }
    public Coord getVelo() {
        return velo;
    }
    public void setVelo(Coord velo) {
        this.velo = velo;
    }

    @Override
    public String toString(){
        return "MovableUpdMsg: id: " + id + " pos: " + pos + " velo: " + velo;
    }

    public static final long serialVersionUID = 114514 + 2;
}

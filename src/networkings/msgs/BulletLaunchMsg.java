package networkings.msgs;
import utils.*;
import gameElements.*;

public class BulletLaunchMsg extends EvtMsg{
    public int id;
    public int tankId;
    // when sending by clinet, id will not be used
    public Coord pos;
    public Coord velo;

    public BulletLaunchMsg(int id, int tankId, Coord pos, Coord velo){
        super();
        this.id = id;
        this.tankId = tankId;
        this.pos = pos;
        this.velo = velo;
    }

    public BulletLaunchMsg(Bullet blt){
        this(blt.getId(), blt.getId(), blt.getPos(), blt.getCurVelo());
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTankId() {
        return tankId;
    }

    public void setTankId(int tankId) {
        this.tankId = tankId;
    }

    @Override
    public String toString(){
        return "BulletLaunchMsg: id: " + id + " tankId: " + tankId + " pos: " + pos + " velo: " + velo;
    }

}

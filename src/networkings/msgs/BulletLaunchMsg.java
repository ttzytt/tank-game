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

}

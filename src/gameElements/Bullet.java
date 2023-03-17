package gameElements;
import networkings.msgs.*;
import utils.*;
public class Bullet extends MovableElement{
    float speed;
    int fromTankID;
    public Bullet(Coord pos, float speed, Direct dir, int id, int tankID){
        super(id);
        this.speed = speed;
        fromTankID = tankID;
        noColObjs.add(fromTankID);
        hp = -1;
        this.pos = pos;
        img = new ImagePanel(Consts.BULLET_IMG);
        curVelo = new Coord(dir, speed);
        size = Consts.INIT_BULLET_SIZE;
        damage = Consts.INIT_BULLET_DAMAGE;
        toRemove = false;
        this.dir = dir;
    }

    // constructor with image 
    public Bullet(Coord pos, float speed, Direct dir, ImagePanel img, int id) {
        super(id);
        this.speed = speed;
        this.pos = pos;
        this.img = img;
        damage = Consts.INIT_BULLET_DAMAGE;
        this.size = Consts.INIT_BULLET_SIZE;
        this.dir = dir;
        curVelo = new Coord(dir, speed);
    }
    boolean toRemove = false; 
    @Override 
    public void mov(long dt){
        Coord npos = pos.add(curVelo.mul(dt / 1000f));
        if (!npos.inRect(size, Coord.zero(), ShVar.mapSize)){
            removeStat = RemStat.TO_REM;
        }
        else
            pos = npos;
    }

    public Bullet(BulletLaunchMsg msg){
        this(msg.pos, msg.velo.getDirSpeed(), msg.velo.getDir(), msg.id, msg.tankId);
    }
}

package gameElements;
import utils.*;

public class Bullet extends MovableElement{
    public Bullet(Coord pos, float speed, Direct dir) {
        hp = -1;
        this.pos = pos;
        img = new ImagePanel(Consts.BULLET_IMG);
        curVelo = new Coord(dir, speed);
        size = Consts.INIT_BULLET_SIZE;
        damage = Consts.INIT_BULLET_DAMAGE;
        toRemove = false;
    }
    // constructor with image 
    public Bullet(Coord pos, float speed, Direct dir, ImagePanel img) {
        this.pos = pos;
        this.img = img;
        damage = Consts.INIT_BULLET_DAMAGE;
        this.size = Consts.INIT_BULLET_SIZE;
        curVelo = new Coord(dir, speed);
    }
    boolean toRemove = false; 
    @Override 
    public void mov(long dt){
        Coord npos = pos.add(curVelo.mul(dt / 1000f));
        if (!npos.inRect(size, Coord.zero(), Consts.mapSize)){
            removeStat = RemoveStat.TO_REM;
        }
        else
            pos = npos;
    }

}

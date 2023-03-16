package gameElements;
import utils.*;

public class BoundingBox extends MovableElement{
    public BoundingBox(Coord pos, Coord siz) {
        super(-1); // undefined id
        this.hp = 0;
        this.pos = pos;
        this.size = siz;
    }   

    public BoundingBox(GameElement ge){
        super(-1);
        this.hp = 0;
        this.pos = ge.getPos();
        this.size = ge.getSize();
        addNoColObj(ge.getId());
    }

    public BoundingBox(MovableElement me){
        super(-1);
        this.hp = 0;
        this.pos = me.getPos();
        this.size = me.getSize();
        this.curVelo = me.getCurVelo();
        addNoColObj(me.getId());
    }
    
    public void mov(MovableElement me, int dt){
        curVelo = me.getCurVelo();
        super.mov(dt);
    }

    @Override
    public String toString(){
        return "bounding box of pos= " + pos + " size= " + size;
    }
}

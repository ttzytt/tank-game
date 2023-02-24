package gameElements;
import utils.*;
import java.util.*;
public abstract class Blks extends GameElement{
    public BlkCoord intPos;
    Blks(BlkCoord initPos){
        size = new Coord(1, 1);
        damage = 0;
        pos = new Coord(initPos.x, initPos.y);
        intPos = initPos;
    }
    public BlkCoord getIntPos() {
        return intPos;
    }
    public void setIntPos(BlkCoord intPos) {
        this.intPos = intPos;
    }

    @Override
    protected boolean processCollision(GameElement other) {
        // check if one tank is in between two blocks
        // if so, do not decrease hp
        if (!(other instanceof Tank)){
            return super.processCollision(other);
        }
        Tank t = (Tank) other;
        TankBlkCollisionAuxInfo auxInfo = new TankBlkCollisionAuxInfo(this, t);
        if ((auxInfo.isTankEnclosed && auxInfo.midOpp instanceof Blks)
            || (auxInfo.isTankOffsetLowerOrLeft && (auxInfo.lowerOpp instanceof Blks || auxInfo.midOpp instanceof Blks))
            || (auxInfo.isTankOffsetHigherOrRight && (auxInfo.upperOpp instanceof Blks || auxInfo.midOpp instanceof Blks))
        ){
            return false;
        }
    
        return super.processCollision(other);
    }

}
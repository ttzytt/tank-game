package gameElements;
import utils.*;
import java.util.*;
public final class DestrBlk extends Blks {
    public DestrBlk(BlkCoord initPos){
        super(initPos);
        hp = Consts.INIT_DESTR_BLK_HP;
        img = new ImagePanel(Consts.DESTR_BLK_IMG);
    }
}
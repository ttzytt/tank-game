package gameElements;
import utils.*;
import java.util.*;
public final class DestrBlk extends Blks {
    public DestrBlk(BlkCoord initPos, int id){
        super(initPos, id);
        hp = Consts.INIT_DESTR_BLK_HP;
        img = new ImagePanel(Consts.DESTR_BLK_IMG);
    }
}
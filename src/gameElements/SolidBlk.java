package gameElements;
import utils.*;
import java.util.*;
public final class SolidBlk extends Blks{
    public SolidBlk(BlkCoord initPos){
        super(initPos);
        img = new ImagePanel(Consts.SOLID_BLK_IMG);
        hp = -1;
    }
}

package networkings.msgs;

import gameElements.GameElement;
import gameElements.MovableElement;
import utils.*;

public class RemEleMsg extends EvtMsg {
    int id;
    public RemEleMsg(int id){
        super();
        this.id = id;
    }
    public RemEleMsg(GameElement ge){
        this(ge.getId());
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}

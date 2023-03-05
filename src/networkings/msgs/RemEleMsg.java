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

    public RemEleMsg(int id, long prio){
        this(id);
        this.prio = prio;
    }

    public RemEleMsg(GameElement ge){
        this(ge.getId());
    }

    public RemEleMsg(GameElement ge, long prio) {
        this(ge.getId(), prio);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return "RemEleMsg: id: " + id;
    }
}

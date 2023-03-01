package networkings.msgs;
import gameElements.*;

public class HPUpdMsg extends EvtMsg {
    public int id;
    public int newHp; // if new HP equals or less than 0, the game element is cleared
    public HPUpdMsg(int id, int newHP){
        super();
        this.id = id;
        this.newHp = newHP;
    }
    public HPUpdMsg(GameElement ge){
        this(ge.getId(), ge.getHp());
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getNewHp() {
        return newHp;
    }
    public void setNewHp(int newHP) {
        this.newHp = newHP;
    }
}

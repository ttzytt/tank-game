package networkings.msgs;
import graphics.*;
import utils.*;
public class MapInitMsg extends EvtMsg{
    int max_x, max_y;
    char mpch[][]; // map code
    int mpid[][];
    public MapInitMsg(GameMap mp){
        super();
        prio = -1;
        max_x = mp.getMax_x();
        max_y = mp.getMax_y();
        mpch = new char[max_x + 1][max_y + 1];
        mpid = new int[max_x + 1][max_y + 1];
        for (int i = 0; i <= max_x; i++){
            for (int j = 0; j <= max_y; j++){
                mpch[i][j] = mp.getEleCh(i, j);
                if (mp.getMap()[i][j] != null)
                mpid[i][j] = mp.getMap()[i][j].getId();
            }
        }
    }

    public int[][] getMpid() {
        return mpid;
    }

    public void setMpid(int[][] mpid) {
        this.mpid = mpid;
    }

    public int getMax_x() {
        return max_x;
    }

    public void setMax_x(int max_x) {
        this.max_x = max_x;
    }

    public int getMax_y() {
        return max_y;
    }

    public void setMax_y(int max_y) {
        this.max_y = max_y;
    }

    public char[][] getMpch() {
        return mpch;
    }

    public void setMpch(char[][] mpch) {
        this.mpch = mpch;
    }

    public static final long serialVersionUID = 114514 + 8;
    int ttest = 1;
}

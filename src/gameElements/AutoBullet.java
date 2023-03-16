
package gameElements;
import utils.*;
import java.util.Stack;
import static utils.ShVar.*;

public class AutoBullet extends Bullet{
    float pathFindingStep;
    Stack<Coord> path;
    public AutoBullet(Coord pos, float speed, Direct dir, int id, int tankID, float step){
        super(pos, speed, dir, id, tankID);
        pathFindingStep = step;
        // randomly chose one tank that is not self for target
        Tank tarTk = null;
        do {
            tarTk = map.getTks().get((int) (Math.random() * map.getTks().size()));
        } while (tarTk.getId() == tankID);
        PathFinder finder = new PathFinder(step,this.getBox(), tarTk.getBox());
        path = finder.getPath();
    }

    @Override
    public void mov(long dt){
        if (path.size() == 0) return;
        Coord nexPosDis = path.peek().sub(pos);
        float mxMovDis = speed * dt / 1000f;
        float actualMovDis = Math.min(nexPosDis.getDirSpeed(), mxMovDis);
        Coord npos = pos.add(nexPosDis.getUnit().mul(actualMovDis));
        if (npos.inRect(size, Coord.zero(), ShVar.mapSize))
            pos = npos;
        if (path.size() > 0 && pos.equals(path.peek())){
            path.pop();
        }
        if (path.size() == 0) {
            removeStat = RemStat.TO_REM;
        }
    }

}

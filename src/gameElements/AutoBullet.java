
package gameElements;

import utils.*;
import java.util.Stack;
import static utils.ShVar.*;

public class AutoBullet extends Bullet {
    float pathFindingStep;
    Stack<Coord> path;

    public AutoBullet(Coord pos, float speed, Direct dir, int id, int tankID, float step) {
        super(pos, speed, dir, id, tankID);
        pathFindingStep = step;
        // randomly chose one tank that is not self for target
        Tank tarTk = null;
        do {
            tarTk = map.getTks().get((int) (Math.random() * map.getTks().size()));
        } while (tarTk.getId() == tankID);
        PathFinder finder = new PathFinder(step, this.getBox(), tarTk.getBox());
        path = finder.getPath();
    }

    @Override
    public void mov(long dt) {
        if (path.size() == 0)
            return;
        if (movToPos(path.peek(), dt)) {
            path.pop();
        }
        if (path.size() == 0) {
            removeStat = RemStat.TO_REM;
        }
    }

}

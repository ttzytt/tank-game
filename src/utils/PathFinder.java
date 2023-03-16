package utils;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Map;
import java.util.ArrayList;
import gameElements.*;
import static utils.ShVar.*;
import java.awt.*;
import gameElements.GameElement.RemStat;
public class PathFinder {
    float step; // may devide the continuous map into many grids
    Coord[] dirs = {new Coord(0, -1), new Coord(1, 0), new Coord(0, 1), new Coord(-1, 0)};
    BoundingBox curBox, tarBox;
    Stack <Coord> path = new Stack<Coord>();
    public static class PathNode implements Comparable<PathNode> {
        BoundingBox box;
        PathNode prev;
        float passedDis, estDis;
        
        @Override
        public int compareTo(PathNode pn) {
            return ((passedDis + estDis) - (pn.passedDis + pn.estDis)) > 0 ? 1 : -1;
        }

        public PathNode(BoundingBox box, PathNode prev, float passedDis, float estDis) {
            this.box = box;
            this.prev = prev;
            this.passedDis = passedDis;
            this.estDis = estDis;
        }
        
        public BoundingBox getSmallBox(){
            return new BoundingBox(box.getPos(), box.getSize().mul(.5f));
        }

        static final PathNode INVAL_NODE = new PathNode(null, null, -1l, -1l);
    }

    public PathFinder(float step, BoundingBox curBox, BoundingBox tarBox) {
        this.step = step;
        this.curBox = curBox;
        this.tarBox = tarBox;
        Queue<PathNode> q = new PriorityQueue<PathNode>();
        q.add(new PathNode(curBox, null, 0, 0));
        Map<Coord, Boolean> visited = new TreeMap<Coord, Boolean>();

        ArrayList<DbgBox> prevBox = map.getDbgBoxes();
        for (DbgBox box : prevBox){
            box.setRemStat(RemStat.TO_REM);
        }

        while(q.size() > 0){
            PathNode curNode = q.poll();
            if (curNode.getSmallBox().intersect(tarBox)){
                // found the path
                while (curNode.prev != null){
                    path.push(curNode.box.getPos());
                    map.addEle(new DbgBox(mapSize, new Coord(.1f, 0.1f), Color.pink, null, getCurId()));
                    curNode = curNode.prev;
                }
                return;
            }
            for (Coord dir : dirs){
                BoundingBox nextBox = new BoundingBox(curNode.box);
                nextBox.setPos(curNode.box.getPos().add(dir.mul(step)));
                // System.out.println("nextBox: " + nextBox.getPos());
                // check if the next box is in the map
                if (!nextBox.getPos().inRect(nextBox.getSize(), Coord.zero(), mapSize)) continue;
                if (visited.containsKey(nextBox.getPos()) && visited.get(nextBox.getPos())){
                    System.out.println("continued");
                    continue;

                }

                ArrayList<BlkCoord> possibleTouchBlks = new ArrayList<>();
                // calculate what are the blocks than have the possibliity to be touched
                BlkCoord flredTopLf = nextBox.getPos().getFlrB();
                BlkCoord ciledBotRt = nextBox.getPos().add(nextBox.getSize()).getFlrB();
                
                for (int i = flredTopLf.x; i <= ciledBotRt.x && i <= (mapSize.x); i++){
                    for (int j = flredTopLf.y; j <= ciledBotRt.y && i <= mapSize.y; j++){
                        possibleTouchBlks.add(new BlkCoord(i, j));
                    }
                }
                // check if the next box is valid
                boolean valid = true;
                for (BlkCoord blkPos : possibleTouchBlks){
                    if (map.getBlk(blkPos) != null && nextBox.intersect(map.getBlk(blkPos))){
                        valid = false;
                        break;
                    }
                }
                if (!valid) continue;

                // check if collide with self

                // add the next box to the queue
                visited.put(nextBox.getPos(), true);
                map.addEle(new DbgBox(nextBox.getPos(), new Coord(.1f, .1f), Color.pink, null, getNexId()));
                // try{
                //     Thread.sleep(100);
                // } catch (Exception e){
                //     e.printStackTrace();
                // }
                q.add(new PathNode(nextBox, curNode, curNode.passedDis + step, nextBox.getPos().dis_man(tarBox.getPos())));
            }
        }
    }

    public Stack<Coord> getPath(){
        return path;
    }
}

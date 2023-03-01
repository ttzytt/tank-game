package utils;

import java.io.Serializable;
import utils.Coord;

public enum Direct {
    UP{
        @Override
        public Direct turnOpp() {
            return DOWN;
        }
        @Override
        public Direct turnRight() {
            return RIGHT;
        }
        @Override
        public Direct turnLeft() {
            return LEFT;
        }
    },
     DOWN{
        @Override
        public Direct turnOpp() {
            return UP;
        }
        @Override
        public Direct turnRight() {
            return LEFT;
        }
        @Override
        public Direct turnLeft() {
            return RIGHT;
        }
    },
     LEFT{
        @Override
        public Direct turnOpp() {
            return RIGHT;
        }
        @Override
        public Direct turnRight() {
            return UP;
        }
        @Override
        public Direct turnLeft() {
            return DOWN;
        }
    },
     RIGHT{
        @Override
        public Direct turnOpp() {
            return LEFT;
        }
        @Override
        public Direct turnRight() {
            return DOWN;
        }
        @Override
        public Direct turnLeft() {
            return UP;
        }
    };
    abstract public Direct turnOpp();
    abstract public Direct turnRight();
    abstract public Direct turnLeft();

    public static Direct getRandDir(){
        int i = (int)(Math.random()*4);
        if (i == 0)
            return UP;
        else if (i == 1)
            return DOWN;
        else if (i == 2)
            return LEFT;
        else
            return RIGHT;   
    }
}

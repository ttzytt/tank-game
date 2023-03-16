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
        @Override
        public Direct turn(Direct other) {
            if (other == UP)
                return UP;
            else if (other == DOWN)
                return DOWN;
            else if (other == LEFT)
                return LEFT;
            else
                return RIGHT;
        }
        @Override
        public Direct getRelDir(Direct other){
            if (other == UP)
                return UP;
            else if (other == DOWN)
                return DOWN;
            else if (other == LEFT)
                return LEFT;
            else
                return RIGHT;
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
        @Override
        public Direct turn(Direct other) {
            if (other == UP)
                return DOWN;
            else if (other == DOWN)
                return UP;
            else if (other == LEFT)
                return RIGHT;
            else
                return LEFT;
        }
        @Override
        public Direct getRelDir(Direct other){
            if (other == UP)
                return DOWN;
            else if (other == DOWN)
                return UP;
            else if (other == LEFT)
                return RIGHT;
            else
                return LEFT;
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
        @Override
        public Direct turn(Direct other) {
            if (other == UP)
                return LEFT;
            else if (other == DOWN)
                return RIGHT;
            else if (other == LEFT)
                return DOWN;
            else
                return UP;
        }
        @Override
        public Direct getRelDir(Direct other){
            if (other == UP)
                return RIGHT;
            else if (other == DOWN)
                return LEFT;
            else if (other == LEFT)
                return UP;
            else
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
        @Override
        public Direct turn(Direct other) {
            if (other == UP)
                return RIGHT;
            else if (other == DOWN)
                return LEFT;
            else if (other == LEFT)
                return UP;
            else
                return DOWN;
        }
        @Override
        public Direct getRelDir(Direct other){
            if (other == UP)
                return LEFT;
            else if (other == DOWN)
                return RIGHT;
            else if (other == LEFT)
                return DOWN;
            else
                return UP;
        }
    };
    abstract public Direct turnOpp();
    abstract public Direct turnRight();
    abstract public Direct turnLeft();
    abstract public Direct turn(Direct other);
    abstract public Direct getRelDir(Direct other);

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

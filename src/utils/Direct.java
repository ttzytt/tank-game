package utils;
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
}

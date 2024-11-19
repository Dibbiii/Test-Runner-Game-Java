package org.unibo.utils;

public class Constats {
    public static class Directions {
        public static final int UP = 0;
        public static final int LEFT = 1;
        public static final int DOWN = 2;
        public static final int RIGHT = 3;
    }

    public static class PlayerConstats {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;

        public static int GetSpriteAmount(int state) {
            switch (state) {
                case IDLE:
                    return 2;
                case RUNNING:
                    return 6;
                case ATTACK:
                    return 2;
                default:
                    return 0;
            }
        }
    }
}

package org.unibo.utils;

import static org.unibo.Game.*;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {
        if (!isSolid(x, y, levelData)) {
            if (!isSolid(x + width, y + height, levelData)) {
                if (!isSolid(x + width, y, levelData)) {
                    if (!isSolid(x, y + height, levelData)) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    private static boolean isSolid(float x, float y, int[][] levelData) {
        if (x < 0 || x >= GAME_WIDTH) {
            return true;
        }
        if (y < 0 || y >= GAME_HEIGHT) {
            return true;
        }

        float xIndex = x / TILES_SIZE;
        float yIndex = y / TILES_SIZE;

        int value = levelData[(int) yIndex][(int) xIndex];

        if (value >= 48 || value < 0 || value != 11) {
            return true;
        }
        return false;
    }
}

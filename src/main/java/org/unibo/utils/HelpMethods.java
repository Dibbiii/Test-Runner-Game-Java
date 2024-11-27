package org.unibo.utils;

import static org.unibo.Game.*;

import java.awt.geom.Rectangle2D;

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
        int maxWidth = levelData[0].length * TILES_SIZE; 

        if (x < 0 || x >= maxWidth) {
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

    // * Get Entity X Position If Next To The Wall
    public static float GetEntityXPosition(Rectangle2D.Float hitBox, float xStep) {
        int currentTile = (int) hitBox.x / TILES_SIZE;

        if (xStep > 0) {
            // right
            int tileXPos = currentTile * TILES_SIZE;
            int xOffset = (int) (TILES_SIZE - hitBox.width);
            return tileXPos + xOffset - 1;
        } else {
            // left
            return currentTile * TILES_SIZE;
        }

    }

    // * Get Entity Y Position If Under The Roof or Abouve The Ground
    public static float GetEntityYPosition(Rectangle2D.Float hitBox, float airSpeed) {
        int currentTile = (int) hitBox.y / TILES_SIZE;

        if (airSpeed > 0) {
            // falling and after touching floor
            int tileYPos = currentTile * TILES_SIZE;
            int yOffset = (int) (TILES_SIZE - hitBox.height);
            return tileYPos + yOffset - 1;
        } else {
            // jumping
            return currentTile * TILES_SIZE;
        }
    }

    public static boolean isOnGround(Rectangle2D.Float hitBox, int[][] levelData) {
        if (!isSolid(hitBox.x, hitBox.y + hitBox.height + 1, levelData)) {
            if (!isSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, levelData)) {
                return false;
            }
        }
        return true;
    }
}

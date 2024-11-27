package org.unibo.handler;

import static org.unibo.Game.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.unibo.Game;
import org.unibo.levels.Level;
import org.unibo.utils.LoadSave;

import static org.unibo.utils.LoadSave.*;

public class LevelHandler {
    private Game game;
    private BufferedImage[] levelSprite;
    private Level level;

    public LevelHandler(Game game) {
        this.game = game;
        importTiles();
        level = new Level(LoadSave.GetLevelData());
    }

    private void importTiles() {
        BufferedImage image = LoadSave.GetSpriteAtlas(TILES_ATLAS);
        levelSprite = new BufferedImage[48];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 12; c++) {
                int index = r * 12 + c;
                levelSprite[index] = image.getSubimage(c * 32, r * 32, 32, 32);
            }
        }
    }

    public Level getLevelData() {
        return level;
    }

    public void renderEndLevelMarker(Graphics g, int levelOffset) {
        int endLevelX = (level.getLevelData()[0].length - 1) * TILES_SIZE - levelOffset;
        int endLevelY = (TILES_HEIGHT - 1) * TILES_SIZE;
        g.setColor(Color.RED);
        g.fillRect(endLevelX, endLevelY, TILES_SIZE, TILES_SIZE);
    }

    public void render(Graphics g, int levelOffSet) {
        for (int r = 0; r < TILES_HEIGHT; r++) {
            for (int c = 0; c < level.getLevelData()[0].length; c++) {
                int index = level.getSpriteIndex(c, r);
                g.drawImage(levelSprite[index], // image
                        (TILES_SIZE * c) - levelOffSet, // x
                        r * TILES_SIZE, // y
                        TILES_SIZE, // width
                        TILES_SIZE, // height
                        null);
            }
        }
    }

    public void update() {

    }

}

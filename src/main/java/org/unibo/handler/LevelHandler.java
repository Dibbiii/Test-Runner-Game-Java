package org.unibo.handler;

import static org.unibo.Game.TILES_HEIGHT;
import static org.unibo.Game.TILES_SIZE;
import static org.unibo.Game.TILES_WIDTH;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.unibo.Game;
import org.unibo.levels.Level;
import org.unibo.utils.LoadSave;

public class LevelHandler {
    public Game game;
    private BufferedImage[] levelSprite;
    private Level level;

    public LevelHandler(Game game) {
        this.game = game;
        importTiles();
        level = new Level(LoadSave.GetLevelData(LoadSave.LEVEL_ATLAS));
    }

    private void importTiles(){
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.TILES_ATLAS);
        levelSprite = new BufferedImage[48];
        for(int r = 0; r < 4; r++){
            for(int c = 0; c < 12; c++){
                int index = r * 12 + c;
                levelSprite[index] = image.getSubimage(c * 32, r * 32, 32, 32);
            }
        }
    }

    public Level getLevelData(){
        return level;
    }

    public void render(Graphics g){
        for(int r = 0; r < TILES_HEIGHT; r++){
            for(int c = 0; c < TILES_WIDTH; c++){
                int index = level.getSpriteIndex(c, r);
                g.drawImage(levelSprite[index], c * TILES_SIZE, r * TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
            }
        }
    }
    
    public void update(){

    }

}

package org.unibo.handler;

import static org.unibo.utils.LoadSave.ENEMY_ATLAS;
import static org.unibo.utils.Constants.EnemyConstants.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.unibo.entities.Crabby;
import org.unibo.entities.Player;
import org.unibo.gameStates.Playing;
import org.unibo.utils.LoadSave;

public class EnemyHandler {

    private Playing playing;
    private BufferedImage[][] enemyArray;
    private ArrayList<Crabby> enemies = new ArrayList<>();

    public EnemyHandler(Playing playing) {
        this.playing = playing;

        loadEnemyImages();
        spawnEnemies();
    }

    private void spawnEnemies() {
        enemies = LoadSave.getEnemies();
        System.out.println("Enemies: " + enemies.size());
    }

    private void loadEnemyImages() {
        enemyArray = new BufferedImage[5][9];
        BufferedImage image = LoadSave.GetSpriteAtlas(ENEMY_ATLAS);
        for (int r = 0; r < enemyArray.length; r++) {
            for (int c = 0; c < enemyArray[r].length; c++) {
                enemyArray[r][c] = image.getSubimage(c * CRABBY_DEFAULT_WIDTH, r * CRABBY_DEFAULT_HEIGHT, CRABBY_DEFAULT_WIDTH, CRABBY_DEFAULT_HEIGHT);
            }
        }
    }

    private void drawEnemies(Graphics g, int xLevelOffSet) {
        for (Crabby c : enemies) {
            g.drawImage(enemyArray[c.getEnemyState()][c.getAnimationIndex()], (int) c.getHitBox().x - xLevelOffSet - CRABBY_DRAWOFFSET_X, (int) c.getHitBox().y - CRABBY_DRAWOFFSET_Y, CRABBY_WIDTH, CRABBY_HEIGHT, null);
        }
    }

    public void update(int[][] levelData, Player player) {
        for (Crabby c : enemies){
            c.update(levelData, player);
        }
    }

    public void render(Graphics g, int xLevelOffSet) {
        drawEnemies(g, xLevelOffSet);
    }
}

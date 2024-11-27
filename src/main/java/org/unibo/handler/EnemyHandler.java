package org.unibo.handler;

import static org.unibo.utils.LoadSave.ENEMY_ATLAS;
import static org.unibo.utils.Constants.EnemyConstants.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.unibo.entities.Enemy;
import org.unibo.gameStates.Playing;
import org.unibo.utils.LoadSave;

public class EnemyHandler {

    private Playing playing;
    private BufferedImage[][] enemyArray;
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();

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
                enemyArray[r][c] = image.getSubimage(c * ENEMY_DEFAULT_WIDTH, r * ENEMY_DEFAULT_HEIGHT, ENEMY_DEFAULT_WIDTH, ENEMY_DEFAULT_HEIGHT);
            }
        }
    }

    private void drawEnemies(Graphics g, int xLevelOffSet) {
        for (Enemy e : enemies) {
            g.drawImage(enemyArray[e.getEnemyState()][e.getAnimationIndex()], (int) e.getHitBox().x - xLevelOffSet, (int) e.getHitBox().y,ENEMY_WIDTH, ENEMY_HEIGHT, null);
        }
    }

    public void update() {
        for (Enemy e : enemies){
            e.update();
        }
    }

    public void render(Graphics g, int xLevelOffSet) {
        drawEnemies(g, xLevelOffSet);
    }
}

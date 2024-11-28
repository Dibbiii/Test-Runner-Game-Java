package org.unibo.gameStates;

import static org.unibo.Game.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.unibo.Game;
import org.unibo.GameState;
import org.unibo.entities.Player;
import org.unibo.handler.EnemyHandler;
import org.unibo.handler.LevelHandler;
import org.unibo.test.Health;
import org.unibo.ui.PausedOverlay;
import org.unibo.utils.LoadSave;

import static org.unibo.utils.LoadSave.*;
import static org.unibo.utils.Constants.Environment.*;

public class Playing extends State implements StateMethods {
    private Player player;
    private LevelHandler levelHandler;
    private EnemyHandler enemyHandler;
    private Health healthBar;
    private PausedOverlay pausedOverlay;

    private boolean paused = false;

    private int xLevelOffSet;
    private int leftBorder = (int) (0.2 * GAME_WIDTH);
    private int rightBorder = (int) (0.8 * GAME_WIDTH);
    private int levelTileWide = LoadSave.GetLevelData()[0].length;
    private int maxTilesOffset = levelTileWide - TILES_WIDTH;
    private int maxLevelOffset = maxTilesOffset * TILES_SIZE;

    private BufferedImage backgroungPlayingImage, bigClouds, smallClouds;
    private int[] smallCloudsPos;
    private Random rnd = new Random();

    public Playing(Game game) {
        super(game);
        initClasses();

        backgroungPlayingImage = LoadSave.GetSpriteAtlas(BACKGROUND_PLAYING);
        bigClouds = LoadSave.GetSpriteAtlas(BIG_CLOUDS);
        smallClouds = LoadSave.GetSpriteAtlas(SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for (int i = 0; i < smallCloudsPos.length; i++) {
            smallCloudsPos[i] = 70 * (int) SCALE + rnd.nextInt(150 * (int) SCALE);
        }
    }

    private void initClasses() {
        levelHandler = new LevelHandler(game);
        enemyHandler = new EnemyHandler(this);
        player = new Player(200, 200, (int) (64 * SCALE), (int) (40 * SCALE));
        healthBar = new Health(5);
        pausedOverlay = new PausedOverlay(this);
        player.loadLevelData(levelHandler.getLevelData().getLevelData());
    }

    public void pauseGame() {
        this.paused = !paused;
    }

    public Player getPlayer() {
        return player;
    }

    public void windowFocusLost() {
        player.setLeft(false);
        player.setRight(false);
        player.setJumping(false);
        player.setDown(false);
        player.setAttacking(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO document why this method is empty
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused) {
            pausedOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused) {
            pausedOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused) {
            pausedOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                player.setJumping(true);
                break;
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_S:
                player.setDown(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setAttacking(true);
                break;
            case KeyEvent.VK_ESCAPE:
                pauseGame();
                break;
            case KeyEvent.VK_E:
                healthBar.setHealth(healthBar.getCurrentHealth() + 1);
                break;
            case KeyEvent.VK_Q:
                healthBar.setHealth(healthBar.getCurrentHealth() - 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                player.setJumping(false);
                break;
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_S:
                player.setDown(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setAttacking(false);
                break;
            case KeyEvent.VK_R:
                GameState.state = GameState.MENU;
                break;
            default:
                break;
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (paused) {
            pausedOverlay.mouseDragged(e);
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitBox().x;
        int diff = playerX - xLevelOffSet;

        if (diff > rightBorder) {
            xLevelOffSet += diff - rightBorder;
        } else if (diff < leftBorder) {
            xLevelOffSet += diff - leftBorder;
        }

        if (xLevelOffSet > maxLevelOffset) {
            xLevelOffSet = maxLevelOffset;
        } else if (xLevelOffSet < 0) {
            xLevelOffSet = 0;
        }
    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 3; i++) {
            g.drawImage(bigClouds, (int) ((i * BIG_CLOUDS_WIDTH) - 0.3 * xLevelOffSet), (int) (204 * SCALE), BIG_CLOUDS_WIDTH, BIG_CLOUDS_HEIGHT, null);
        }
        for (int i = 0; i < smallCloudsPos.length; i++) {
            g.drawImage(smallClouds, (int) ((SMALL_CLOUDS_WIDTH * 4 * i) - 0.7 * xLevelOffSet), smallCloudsPos[i], SMALL_CLOUDS_WIDTH, SMALL_CLOUDS_HEIGHT, null);
        }
    }
    
    @Override
    public void update() {
        if (!paused) {
            levelHandler.update();
            player.update();
            enemyHandler.update(levelHandler.getLevelData().getLevelData(), player);
            checkCloseToBorder();
            healthBar.update();
        } else {
            pausedOverlay.update();
        }
    }

    @Override
    public void render(Graphics g) {

        g.drawImage(backgroungPlayingImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        drawClouds(g);

        levelHandler.render(g, xLevelOffSet);
        player.render(g, xLevelOffSet);
        enemyHandler.render(g, xLevelOffSet);
        levelHandler.renderEndLevelMarker(g, xLevelOffSet);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pausedOverlay.render(g);
        }
    }
}

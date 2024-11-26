package org.unibo.gameStates;

import static org.unibo.Game.SCALE;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import org.unibo.Game;
import org.unibo.GameState;
import org.unibo.entities.Player;
import org.unibo.handler.LevelHandler;
import org.unibo.test.Health;
import org.unibo.ui.PausedOverlay;

public class Playing extends State implements StateMethods {

    private Player player;
    private LevelHandler levelHandler;
    private Health healthBar;
    private PausedOverlay pausedOverlay;
    private boolean paused;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        levelHandler = new LevelHandler(game);
        player = new Player(150, 150, (int) (64 * SCALE), (int) (40 * SCALE));
        healthBar = new Health(5);
        pausedOverlay = new PausedOverlay(this);
        player.loadLevelData(levelHandler.getLevelData().getLevel());
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
        }
    }

    @Override
    public void update() {
        if (!paused) {
            levelHandler.update();
            player.update();
            healthBar.update();
        } else {
            pausedOverlay.update();
        }
    }

    @Override
    public void render(Graphics g) {
        levelHandler.render(g);
        player.render(g);
        healthBar.render(g);
        if (paused) {
            pausedOverlay.render(g);
        }
    }
}

package org.unibo.gameStates;

import static org.unibo.Game.SCALE;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import org.unibo.Game;
import org.unibo.GameState;
import org.unibo.entities.Player;
import org.unibo.handler.LevelHandler;

public class Playing extends State implements StateMethods {

    private Player player;
    private LevelHandler levelHandler;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        levelHandler = new LevelHandler(game);
        player = new Player(150, 150, (int) (64 * SCALE), (int) (40 * SCALE));
        player.loadLevelData(levelHandler.getLevelData().getLevel());
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
    public void update() {
        levelHandler.update();
        player.update();
    }

    @Override
    public void render(Graphics g) {
        levelHandler.render(g);
        player.render(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
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
                GameState.state = GameState.MENU;
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
            case KeyEvent.VK_ESCAPE:
                GameState.state = GameState.MENU;
                break;
        }
    }
}

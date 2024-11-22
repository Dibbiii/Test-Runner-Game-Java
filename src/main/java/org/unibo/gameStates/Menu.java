package org.unibo.gameStates;

import static org.unibo.Game.GAME_HEIGHT;
import static org.unibo.Game.GAME_WIDTH;
import static org.unibo.Game.SCALE;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import org.unibo.Game;
import org.unibo.GameState;
import org.unibo.ui.MenuButton;
import org.unibo.utils.LoadSave;

public class Menu extends State implements StateMethods {
    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundImage;
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game) {
        super(game);
        loadButtons();
        laodBackground();
    }

    private void laodBackground() {
        backgroundImage =  LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImage.getWidth() * SCALE);
        menuHeight = (int) (backgroundImage.getHeight() * SCALE);
        menuX = (GAME_WIDTH / 2) - (menuWidth / 2);
        menuY = (GAME_HEIGHT / 2) - (menuHeight / 2);
    }


    // TODO: Implement a way to automatically laod buttons at the center of the screen
    private void loadButtons() {
        buttons[0] = new MenuButton(GAME_WIDTH / 2, (int) (((GAME_HEIGHT / 2) - 90) * SCALE), 0, GameState.PLAYING);
        buttons[1] = new MenuButton(GAME_WIDTH / 2, (int) (((GAME_HEIGHT / 2) - 30) * SCALE), 1, GameState.OPTIONS);
        buttons[2] = new MenuButton(GAME_WIDTH / 2, (int) (((GAME_HEIGHT / 2) + 30) * SCALE), 2, GameState.QUIT);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isInside(e, mb)) {
                mb.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isInside(e, mb)) {
                if (mb.isMousePressed()) {
                    mb.setMousePressed(false);
                    mb.setGameState();
                }
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for (MenuButton mb : buttons) {
            mb.resetBooleans();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons) {
            mb.setMouseOver(false);
        }

        for (MenuButton mb : buttons) {
            if (isInside(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameState.state = GameState.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void update() {
        for (MenuButton mb : buttons) {
            mb.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(backgroundImage, menuX, menuY, menuWidth, menuHeight, null);
        for (MenuButton mb : buttons) {
            mb.render(g);
        }
    }
}
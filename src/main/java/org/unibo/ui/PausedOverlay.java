package org.unibo.ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import org.unibo.GameState;
import org.unibo.gameStates.*;
import org.unibo.utils.LoadSave;

import static org.unibo.Game.*;
import static org.unibo.utils.Constants.UI.PauseButtons.*;
import static org.unibo.utils.Constants.UI.URMButtons.*;

public class PausedOverlay {
    private Playing playing;
    private BufferedImage overlayImage;
    private int x, y, width, height;

    private SoundButton musicButton, sfxButton;
    private UrmButton unpausedButton, replayButton, menuButton;

    public PausedOverlay(Playing playing) {
        this.playing = playing;

        loadOverlay();
        createSoundButtons();
        createUrmButtons();
    }

    private void createUrmButtons() {
        int unpausedX = 462 * (int) SCALE;
        int replayX = 387 * (int) SCALE;
        int menuX = 313 * (int) SCALE;
        int buttonY = 325 * (int) SCALE;

        unpausedButton = new UrmButton(unpausedX, buttonY, URM_SIZE, URM_SIZE, 0);
        replayButton = new UrmButton(replayX, buttonY, URM_SIZE, URM_SIZE, 1);
        menuButton = new UrmButton(menuX, buttonY, URM_SIZE, URM_SIZE, 2);
    }

    private void createSoundButtons() {
        int soundX = 450 * (int) SCALE;
        int musicY = 140 * (int) SCALE;
        int sfxY = 186 * (int) SCALE;
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void loadOverlay() {
        overlayImage = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        width = overlayImage.getWidth() * (int) SCALE;
        height = overlayImage.getHeight() * (int) SCALE;
        x = GAME_WIDTH / 2 - width / 2;
        y = 25 * (int) SCALE;

    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton)) {
            musicButton.setMousePressed(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        } else if (isIn(e, unpausedButton)) {
            unpausedButton.setMousePressed(true);
        } else if (isIn(e, replayButton)) {
            replayButton.setMousePressed(true);
        } else if (isIn(e, menuButton)) {
            menuButton.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.setMuted(!musicButton.isMuted());
            }
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.setMuted(!sfxButton.isMuted());
            }
        } else if (isIn(e, unpausedButton)) {
            if (unpausedButton.isMousePressed()) {
                playing.pauseGame();
            }
        } else if (isIn(e, replayButton)) {
            if (replayButton.isMousePressed()) {
                System.out.println("Replay");
            }
        } else if (isIn(e, menuButton)) {
            if (menuButton.isMousePressed()) { 
                playing.pauseGame();
                GameState.state = GameState.MENU;
            }
        }

        musicButton.resetBooleans();
        sfxButton.resetBooleans();
        unpausedButton.resetBooleans();
        replayButton.resetBooleans();
        menuButton.resetBooleans();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        unpausedButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        menuButton.setMouseOver(false);

        if (isIn(e, musicButton)) {
            musicButton.setMouseOver(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        } else if (isIn(e, unpausedButton)) {
            unpausedButton.setMouseOver(true);
        } else if (isIn(e, replayButton)) {
            replayButton.setMouseOver(true);
        } else if (isIn(e, menuButton)) {
            menuButton.setMouseOver(true);
        }
    }

    public void mouseDragged(MouseEvent e) {
    }

    private boolean isIn(MouseEvent e, PauseButton btn) {
        return btn.getBounds().contains(e.getX(), e.getY());
    }

    public void update() {
        musicButton.update();
        sfxButton.update();
        unpausedButton.update();
        replayButton.update();
        menuButton.update();
    }

    public void render(Graphics g) {
        g.drawImage(overlayImage, x, y, width, height, null);

        musicButton.render(g);
        sfxButton.render(g);
        unpausedButton.render(g);
        replayButton.render(g);
        menuButton.render(g);
    }
}

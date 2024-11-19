package org.unibo;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.unibo.handler.KeyboardHandler;
import org.unibo.handler.MouseHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.unibo.utils.Constats.PlayerConstats.*;
import static org.unibo.utils.Constats.Directions.*;

public class GamePanel extends JPanel {
    private MouseHandler mouseHandler;
    private float x, y = 10;
    private BufferedImage image;
    private BufferedImage[][] animations;
    private int animTick = 0, animIndex = 0, animSpeed = 120 / 6;
    private int playerState = RUNNING;
    private int playerDirection = -1;
    private boolean isMoving = false;

    private int playerSpeed = 2;

    public GamePanel() {
        mouseHandler = new MouseHandler();
        importImage();
        loadAnimations();
        addKeyListener(new KeyboardHandler(this));
        setPanelSize();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    private void loadAnimations() {
        animations = new BufferedImage[3][6];

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 6; c++) {
                animations[r][c] = image.getSubimage(c * 24, r * 30, 24, 30);
            }
        }
    }

    private void importImage() {
        InputStream is = getClass().getResourceAsStream("/sprite.png");
        try {
            if (is == null) {
                throw new IllegalArgumentException("Image file not found");
            }
            image = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Image file not found: " + e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setPanelSize() {
        Dimension size = new Dimension(600, 400);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public void setDirection(int direction) {
        this.playerDirection = direction;
        setMoving(true);
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    private void updateAnimTick() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
            if (animIndex >= GetSpriteAmount(playerState)) {
                animIndex = 0;
            }
        }
    }

    private void setAnimation() {
        if (isMoving) {
            playerState = RUNNING;
        } else {
            playerState = IDLE;
        }
    }

    private void updatePosition() {
        if (isMoving) {
            switch (playerDirection) {
                case UP:
                    y -= playerSpeed;
                    break;
                case DOWN:
                    y += playerSpeed;
                    break;
                case LEFT:
                    x -= playerSpeed;
                    break;
                case RIGHT:
                    x += playerSpeed;
                    break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateAnimTick();

        setAnimation();
        updatePosition();

        g.drawImage(animations[playerState][animIndex], (int) x, (int) y, 24 * 4, 30 * 4, null);
    }

}

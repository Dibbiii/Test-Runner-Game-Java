package org.unibo.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.unibo.utils.LoadSave;

import static org.unibo.Game.SCALE;
import static org.unibo.utils.Constats.PlayerConstats.*;
import static org.unibo.utils.HelpMethods.*;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private boolean isMoving, isAttacking = false;
    private int playerState = IDLE;
    private int animTick = 0, animIndex = 0, animSpeed = 120 / 6;
    private boolean left, right, up, down;

    private float playerStep = 0.6f;

    private int[][] levelData;
    
    private float xDrawOffSet = 0 * SCALE, yDrawOffSet = 0 * SCALE;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitBox(x, y, width * SCALE, height * SCALE);
    }

    private void loadAnimations() {
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[3][6];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 6; c++) {
                animations[r][c] = image.getSubimage(c * 24, r * 30, 24, 30);
            }
        }
    }

    private void updateAnimTick() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
            if (animIndex >= GetSpriteAmount(playerState)) {
                animIndex = 0;
                isAttacking = false;
            }
        }
    }

    private void setAnimation() {
        int startAnim = playerState;

        if (isMoving) {
            playerState = RUNNING;
        } else {
            playerState = IDLE;
        }

        if (isAttacking) {
            playerState = ATTACK;
        }

        if (startAnim != playerState) {
            resetAnimTick();
        }
    }

    private void resetAnimTick() {
        animTick = 0;
        animIndex = 0;
    }

    private void updatePosition() {
        isMoving = false;
        if (!left && !right && !up && !down) {
            return;
        }   

        float xStep = 0, yStep = 0;

        if (left && !right) {
            xStep =- playerStep;
        } else if (right && !left) {
            xStep = playerStep;
        }

        if (up && !down) {
            yStep -= playerStep;
        } else if (down && !up) {
            yStep = playerStep;
        }

        if (CanMoveHere(hitBox.x + xStep, hitBox.y + yStep, hitBox.width, hitBox.height, levelData)) {
            hitBox.x += xStep;
            hitBox.y += yStep;
            isMoving = true;
        }
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
    }

    public void update() {
        // Update player
        updatePosition();
        updateAnimTick();
        setAnimation();
    }

    public void render(Graphics g) {
        // Render player
        g.drawImage(animations[playerState][animIndex], (int) (hitBox.x - xDrawOffSet), (int) (hitBox.y - yDrawOffSet), width, height, null);
        drawHitBox(g);
    }
}

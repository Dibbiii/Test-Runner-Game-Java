package org.unibo.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.unibo.utils.LoadSave;

import static org.unibo.utils.LoadSave.*;
import static org.unibo.utils.Constants.PlayerConstats.*;
import static org.unibo.utils.HelpMethods.*;
import static org.unibo.Game.*;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private boolean isMoving = false, isAttacking = false, isInAir = false, isJumping = false;
    private int playerState = IDLE;
    private int animTick = 0, animIndex = 0, animSpeed = FPS / 3;
    private boolean left, right, down;
    private float playerStep = 1.0f * SCALE;

    // * Jumping / Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * SCALE;
    private float jumpSpeed = -2.4f * SCALE;
    private float fallSpeedAfterCollision = 0.5f * SCALE;

    // * 
    private BufferedImage statusBarImage;

    // * Posiition and Size of the status bar Image
	private int statusBarWidth = (int) (192 * SCALE);
	private int statusBarHeight = (int) (58 * SCALE);
	private int statusBarX = (int) (10 * SCALE);
	private int statusBarY = (int) (10 * SCALE);

    // * Position and Size of the health bar
	private int healthBarWidth = (int) (150 * SCALE);
	private int healthBarHeight = (int) (4 * SCALE);
	private int healthBarXStart = (int) (34 * SCALE);
	private int healthBarYStart = (int) (14 * SCALE);

	private int maxHealth = 10;
	private int currentHealth = maxHealth;
	private int healthWidth = healthBarWidth;

    private int[][] levelData;

    private float xDrawOffSet = 21 * SCALE, yDrawOffSet = 4 * SCALE;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitBox(x, y, (int) (20 * SCALE), (int) (27 * SCALE));
    }

    private void loadAnimations() {
        BufferedImage image = LoadSave.GetSpriteAtlas(PLAYER_ATLAS);

        animations = new BufferedImage[9][6];
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                animations[r][c] = image.getSubimage(c * 64, r * 40, 64, 40);
            }
        }

        statusBarImage = LoadSave.GetSpriteAtlas(STATUS_BAR);
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

        if (isInAir) {
            if (airSpeed < 0) {
                playerState = JUMPING;
            } else {
                playerState = FALLING;
            }
        }

        if (isAttacking) {
            playerState = ATTACKING;
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

        if (isJumping) {
            jump();
        }

        if (!isInAir && (!left && !right || left && right)) {
            return;
        }

        float xStep = 0;

        if (left) {
            xStep -= playerStep;
        }
        if (right) {
            xStep += playerStep;
        }

        if (!isInAir && !isOnGround(hitBox, levelData)) {
            isInAir = true;
        }

        if (isInAir) {
            if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, levelData)) {
                hitBox.y += airSpeed;
                airSpeed += gravity;
                updateXPosition(xStep);
            } else {
                hitBox.y = GetEntityYPosition(hitBox, airSpeed);
                if (airSpeed > 0) {
                    resetIsInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPosition(xStep);
            }
        } else {
            updateXPosition(xStep);
        }
        isMoving = true;
    }

    private void updateXPosition(float xStep) {
        if (CanMoveHere(hitBox.x + xStep, hitBox.y, hitBox.width, hitBox.height, levelData)) {
            hitBox.x += xStep;
        } else {
            hitBox.x = GetEntityXPosition(hitBox, xStep);
        }
    }

    public void changeHealth(int value) {
		currentHealth += value;

		if (currentHealth <= 0)
			currentHealth = 0;
		else if (currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}

    public void jump() {
        if (isInAir) {
            return;
        }
        isInAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetIsInAir() {
        isInAir = false;
        airSpeed = 0;
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

    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean isJumping) {
        this.isJumping = isJumping;
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
        System.out.println("Loading level data");
        this.levelData = levelData;
        System.out.println("Level Data " + levelData.length);
        if (!isOnGround(hitBox, levelData)) {
            isInAir = true;
        }
    }

    private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
	}

    private void drawUI(Graphics g) {
		g.drawImage(statusBarImage, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
	}

    public void update() {
        // Update player
        updateHealthBar();
        updatePosition();
        updateAnimTick();
        setAnimation();
    }

    public void render(Graphics g, int levelOffSet) {
        g.drawImage(animations[playerState][animIndex], (int) (hitBox.x - xDrawOffSet) - levelOffSet,
                (int) (hitBox.y - yDrawOffSet),
                width, height, null);
        drawUI(g);        
    }

    public void resetPlayerBoolean() {
        this.left = false;
        this.right = false;
        this.isJumping = false;
        this.down = false;
        this.isAttacking = false;
    }
}

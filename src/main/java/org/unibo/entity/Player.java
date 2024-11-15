package org.unibo.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.unibo.GamePanel;
import org.unibo.KeyHandler;

public class Player extends Entity {
    GamePanel gameP;
    KeyHandler keyH;

    public Player(GamePanel gameP, KeyHandler keyH) {
        this.gameP = gameP;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        // * Get the player's image
        try {
            //stay = ImageIO.read(getClass().getResourceAsStream("assets/player/stay.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("../../../../../assets/player/up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("../../../../../assets/player/up2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("../../../../../assets/player/down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("../../../../../assets/player/down2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("../../../../../assets/player/left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("../../../../../assets/player/left2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("../../../../../assets/player/right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("../../../../../assets/player/right2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Image file not found: " + e.getMessage());
        }
    }

    public void update() {
        if (keyH.upPressed) {
            direction = "up";
            y -= speed;
        }
        if (keyH.downPressed) {
            direction = "down";
            y += speed;
        }
        if (keyH.leftPressed) {
            direction = "left";
            x -= speed;
        }
        if (keyH.rightPressed) {
            direction = "right";
            x += speed;
        }
    }

    public void draw(Graphics2D g2d) {
        BufferedImage img = null;

        switch (direction) {
            case "up":
                img = up1;
                break;
            case "down":
                img = down1;
                break;
            case "left":
                img = left1;
                break;
            case "right":
                img = right1;
                break;
        }
        g2d.drawImage(img, x, y, gameP.tileSize, gameP.tileSize, null);
    }
}

package org.unibo.utils;

import static org.unibo.Game.*;
import static org.unibo.utils.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.unibo.entities.Crabby;

public class LoadSave {

    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String TILES_ATLAS = "tiles.png";
    public static final String LEVEL_ATLAS = "level_long.png";
    public static final String MENU_BUTTON = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTON = "sound_button.png";
    public static final String VOLUME_BUTTON = "volume_buttons.png";
    public static final String URM_BUTTON = "urm_buttons.png"; // URM = unpause, replay, main menu
    public static final String BACKGROUND_MENU = "background_menu.png";
    public static final String BACKGROUND_PLAYING = "playing_bg_img.png";
    public static final String BIG_CLOUDS = "big_clouds.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";
    public static final String ENEMY_ATLAS = "enemy_sprites.png";


    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage image = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
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
        return image;
    }

    public static ArrayList<Crabby> getEnemies() {
        BufferedImage image = GetSpriteAtlas(LEVEL_ATLAS);
        ArrayList<Crabby> enemies = new ArrayList<Crabby>();
        for (int r = 0; r < image.getHeight(); r++) {
            for (int c = 0; c < image.getWidth(); c++) {
                Color color = new Color(image.getRGB(c, r));
                int value = color.getGreen();
                if (value == CRABBY) {
                    enemies.add(new Crabby(c * TILES_SIZE, r * TILES_SIZE));
                }
            }
        }
        return enemies;
    }

    public static int[][] GetLevelData() {
        BufferedImage image = GetSpriteAtlas(LEVEL_ATLAS);
        int[][] levelData = new int[image.getHeight()][image.getWidth()];

        for (int r = 0; r < image.getHeight(); r++) {
            for (int c = 0; c < image.getWidth(); c++) {
                Color color = new Color(image.getRGB(c, r));
                int value = color.getRed();
                if(value>= 48){
                    value = 0;
                }
                levelData[r][c] = value; 
            } 
        }
        return levelData;
    }
}

package org.unibo.utils;

import static org.unibo.Game.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

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

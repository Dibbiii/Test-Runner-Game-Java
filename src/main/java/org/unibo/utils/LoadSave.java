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
    public static final String LEVEL_ATLAS = "level.png";
    public static final String MENU_BUTTON = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";

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

    public static int[][] GetLevelData(String fileName) {
        int[][] levelData = new int[TILES_HEIGHT][TILES_WIDTH];
        BufferedImage image = GetSpriteAtlas(LEVEL_ATLAS);
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

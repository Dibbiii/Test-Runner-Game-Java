package org.unibo.utils;

import static org.unibo.Game.*;
import static org.unibo.utils.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.unibo.entities.Crabby;

public class LoadSave {
    private static final String SAVES_FOLDER = "src/main/assets/saves/";

    // * Menu Sprites
    public static final String MENU_BUTTON = "menu/button_atlas.png";
    public static final String MENU_BACKGROUND = "menu/menu_background.png";
    public static final String PAUSE_BACKGROUND = "menu/pause_menu.png";
    public static final String SOUND_BUTTON = "menu/sound_button.png";
    public static final String VOLUME_BUTTON = "menu/volume_buttons.png";
    public static final String URM_BUTTON = "menu/urm_buttons.png"; // URM = unpause, replay, main menu
    public static final String BACKGROUND_MENU = "menu/background_menu.png";

    // * Tiles Sprites
    public static final String TILES_ATLAS = "tiles/tiles.png";

    // * Envirovement Sprites
    public static final String BACKGROUND_PLAYING = "envirovement/playing_bg_img.png";
    public static final String BIG_CLOUDS = "envirovement/big_clouds.png";
    public static final String SMALL_CLOUDS = "envirovement/small_clouds.png";

    // * Entites Sprites
    public static final String PLAYER_ATLAS = "entities/player_sprites.png";
    public static final String ENEMY_ATLAS = "entities/enemy_sprites.png";

    // * UI Sprites
    public static final String STATUS_BAR = "ui/health_power_bar.png";

    // * Level Map Sprites
    public static String WAGOON_START = "wagoons/1.png";
    public static String[] WAGOONS = {
        "wagoons/2.png",
        "wagoons/3.png",
        "wagoons/4.png",
    };
    public static String WAGOON_END = "wagoons/5.png";

    public static void shuffleWagoons() {
        // Convert the array to a list
        List<String> wagoonsList = new ArrayList<>(Arrays.asList(WAGOONS));

        // Shuffle the list
        Collections.shuffle(wagoonsList);
        System.out.println("WagoonsList: " + wagoonsList);

        // Add the start and end wagoon to the front and back of the list
        wagoonsList.add(0, WAGOON_START);
        wagoonsList.add(WAGOON_END);

        // Convert the list back to an array
        WAGOONS = wagoonsList.toArray(new String[0]);
        System.out.println("Wagoons: " + Arrays.toString(WAGOONS));
    }

    public static void loadSavesFile() {
        System.out.println("Loading saves files...");
        existsSavesFolder();
        if (isSavesFolderEmpty()) {
            System.out.println("Folder is empty");
            createFile();
        } else {
            System.out.println("Folder is not empty");
            getSavesFiles();
        }
    }

    private static void getSavesFiles() {
        try {
            List<Path> files = Files.list(Paths.get(SAVES_FOLDER))
                    .filter(Files::isRegularFile)
                    .sorted(Comparator.comparingLong(p -> ((Path) p).toFile().lastModified()).reversed())
                    .collect(Collectors.toList());
            for (Path p : files) {
                System.out.println(p.getFileName());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while getting the Saves files.");
            e.printStackTrace();
        }
    }

    public static void createFile() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String formattedDateTime = now.format(formatter);
        String filePath = SAVES_FOLDER + "save_" + formattedDateTime + ".yaml";
        System.out.println("Creating YAML file...");

        try {
            String message = "message: jjjj";
            String createdAt = "created_at: " + formattedDateTime;
            Files.write(Paths.get(filePath), Arrays.asList(message, createdAt));
            System.out.println("YAML file created successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while creating the Saves file.");
            e.printStackTrace();
        }
        appendLineToFile(filePath, "new line");
    }

    public static void appendLineToFile(String filePath, String newLine) {
        try {
            // Read all lines from the file
            List<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(filePath)));
            // Add the new line
            lines.add(newLine);
            // Write all lines back to the file
            Files.write(Paths.get(filePath), lines);
            System.out.println("Line appended successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while appending the line to the file.");
            e.printStackTrace();
        }
    }

    private static void existsSavesFolder() {
        try {
            Path path = Paths.get(SAVES_FOLDER);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while looking if the folder exists.");
            e.printStackTrace();
        }
    }

    public static boolean isSavesFolderEmpty() {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(SAVES_FOLDER))) {
            return !directoryStream.iterator().hasNext();
        } catch (IOException e) {
            System.out.println("An error occurred while checking if the folder is empty.");
            e.printStackTrace();
        }
        return false;
    }

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
        ArrayList<Crabby> enemies = new ArrayList<Crabby>();
        int xOffset = 0;

        for (String wagoon : WAGOONS) {
            BufferedImage image = GetSpriteAtlas(wagoon);
            int imageHeight = image.getHeight();
            int imageWidth = image.getWidth();
            for (int r = 0; r < imageHeight; r++) {
                for (int c = 0; c < imageWidth; c++) {
                    Color color = new Color(image.getRGB(c, r));
                    int value = color.getGreen();
                    if (value == CRABBY) {
                        enemies.add(new Crabby((c + xOffset) * TILES_SIZE, r * TILES_SIZE));
                    }
                }
            }
            xOffset += imageWidth;
        }
        return enemies;
    }

    public static int[][] GetLevelData(String fileName) {
        BufferedImage image = GetSpriteAtlas(fileName);
        int[][] levelData = new int[image.getHeight()][image.getWidth()];

        for (int r = 0; r < image.getHeight(); r++) {
            for (int c = 0; c < image.getWidth(); c++) {
                Color color = new Color(image.getRGB(c, r));
                int value = color.getRed();
                if (value >= 48) {
                    value = 0;
                }
                levelData[r][c] = value;
            }
        }
        return levelData;
    }

    private static int calcFullLevelsWidth() {
        int fullLevelWidth = 0;
        for (int i = 0; i < WAGOONS.length; i++) {
            BufferedImage image = GetSpriteAtlas(WAGOONS[i]);
            fullLevelWidth += image.getWidth();
        }
        return fullLevelWidth;
    }

    public static int getFullLevelWitdh() {
        return calcFullLevelsWidth();
    }
}
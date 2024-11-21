package org.unibo;

import java.awt.Graphics;

import org.unibo.entities.Player;
import org.unibo.handler.LevelHandler;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;

    private final int FPS = 120;
    private final int UPS = 200;

    private Player player;
    private LevelHandler levelHandler;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.0f;
    public final static int TILES_WIDTH = 26;
    public final static int TILES_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_WIDTH * TILES_SIZE;
    public final static int GAME_HEIGHT = TILES_HEIGHT * TILES_SIZE;

    public Game() {
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
    }

    private void initClasses() {
        levelHandler = new LevelHandler(this);
        player = new Player(200, 200, (int) (24 * SCALE), (int) (30 * SCALE));
        player.loadLevelData(levelHandler.getLevelData().getLevel());
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        player.update();
        levelHandler.update();
    }

    public void render(Graphics g) {
        levelHandler.render(g);
        player.render(g);
    }

    @Override
    public void run() {
        // FPS variables
        double timePerFrame = 1_000_000_000.0 / FPS;
        int frames = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaFPS = 0;

        // UPS variables
        double timeperUpdate = 1_000_000_000 / UPS;
        int updates = 0;
        long previousTime = System.nanoTime();
        double deltaUPS = 0;

        while (true) {
            long currentTime = System.nanoTime();

            // UPS loop
            deltaUPS += (currentTime - previousTime) / timeperUpdate;
            deltaFPS += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;
            if (deltaUPS >= 1) {
                update();
                updates++;
                deltaUPS--;
            }

            // FPS loop
            if (deltaFPS >= 1) {
                gamePanel.repaint();
                frames++;
                deltaFPS--;
            }

            // FPS Println
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("Frames: " + frames + " | Updates: " + updates);
                updates = 0;
                frames = 0;
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void windowFocusLost() {
        player.setLeft(false);
        player.setRight(false);
        player.setUp(false);
        player.setDown(false);
        player.setAttacking(false);
    }
}

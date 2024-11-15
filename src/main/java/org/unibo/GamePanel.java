package org.unibo;


import javax.swing.*;

import org.unibo.entity.Player;

import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;
    
    public final int tileSize = originalTileSize * scale; // 48 pixels
    
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // * Set the player's starting position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    // * FPS tracking variables
    int frameCount = 0;
    long lastTime = System.nanoTime();
    double fps = 0;

    // * Handler 
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    // * instance Player
    Player player = new Player(this, keyH);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(keyH);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / 60; // 60 FPS
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            player.update();
            repaint();

            frameCount++;
            long currentTime = System.nanoTime();
            if (currentTime - lastTime >= 1000000000) {
                fps = frameCount;
                frameCount = 0;
                lastTime = currentTime;
                System.out.println("FPS: " + fps);
            }

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000; // Convert to milliseconds

                if (remainingTime < 0) remainingTime = 0;
                Thread.sleep((long) (remainingTime));
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        player.draw(g2d);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        gamePanel.startGameThread();
    }
}
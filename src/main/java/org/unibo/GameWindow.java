package org.unibo;
import javax.swing.JFrame;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;

public class GameWindow {
    private JFrame frame;

    public GameWindow(GamePanel gamePanel) {    
        frame = new JFrame("Game");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(gamePanel);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack(); // This will resize the frame to fit the panel
        frame.setVisible(true);
        frame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
            }
            
            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }
        });
    }
}

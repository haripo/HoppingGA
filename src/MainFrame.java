import Physics.PhysicsSimulator;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

public class MainFrame extends JFrame {
    private Canvas canvas;

    private KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    canvas.moveLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    canvas.moveRight();
                    break;
                case KeyEvent.VK_UP:
                    canvas.zoomUp();
                    break;
                case KeyEvent.VK_DOWN:
                    canvas.zoomDown();
                    break;
            }
        }
    };

    public Canvas getCanvas() {
        return canvas;
    }

    public MainFrame() {
        canvas = new Canvas();
        getContentPane().add(canvas);

        setTitle("HoppingGA");
        setBounds(0, 0, 1000, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        addKeyListener(keyAdapter);
    }
}

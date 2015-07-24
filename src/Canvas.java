
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.JComponent;

public class Canvas extends JComponent {
    private Image buffer;

    private float scale = 40f;
    private float shiftX = 3f;
    private float shiftY = 3f;

    private Graphics getBufferGraphics() {
        Graphics2D graphics = (Graphics2D) buffer.getGraphics();
        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return graphics;
    }

    private ComponentListener resizeListener = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            buffer = createImage(getWidth(), getHeight());
        }
    };

    public Canvas() {
        super();
        addComponentListener(resizeListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(buffer, 0, 0, null);
    }

    public void fill() {
        Graphics graphics = getBufferGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    public void drawCircle(float x, float y, float r) {
        Graphics graphics = getBufferGraphics();
        graphics.drawArc(
                Math.round((x - r + shiftX) * scale),
                Math.round((y - r + shiftY) * scale),
                Math.round(r * scale * 2),
                Math.round(r * scale * 2),
                0, 360);
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        Graphics graphics = getBufferGraphics();
        graphics.drawLine(
                Math.round((x1 + shiftX) * scale), Math.round((y1 + shiftY) * scale),
                Math.round((x2 + shiftX) * scale), Math.round((y2 + shiftY) * scale));
    }

    public void drawInfoString(HashMap<String, String> info) {
        Graphics graphics = getBufferGraphics();
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int lineHeight = fontMetrics.getAscent();
        int y = 15;
        for (String key : info.keySet()) {
            graphics.drawString(key + ": " + info.get(key), 15, y);
            y += lineHeight;
        }
    }
}

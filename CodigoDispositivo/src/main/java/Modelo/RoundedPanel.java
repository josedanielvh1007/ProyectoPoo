package Modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/*
 RoundedPanel.java
 Container that paints a subtle rounded area and a reactive glow around the piano bounds.
*/

public class RoundedPanel extends JPanel {
    private float glow = 0f;
    private Rectangle pianoBounds;
    private final int w, h;

    public RoundedPanel(int w, int h) { this.w = w; this.h = h; setOpaque(false); }

    public void setPianoBounds(Rectangle r) { this.pianoBounds = r; }
    public void increaseGlow() { glow = Math.min(1f, glow + 0.25f); repaint(); }
    public void decreaseGlow() { glow = Math.max(0f, glow - 0.02f); repaint(); }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = Painter.smooth(g);
        RoundRectangle2D.Float base = new RoundRectangle2D.Float(0, 0, w-1, h-1, 20, 20);
        g2.setColor(new Color(0,0,0,30));
        g2.fill(base);

        if (pianoBounds != null) {
            int x = pianoBounds.x, y = pianoBounds.y, pw = pianoBounds.width, ph = pianoBounds.height;
            Color glowColor = new Color(0,180,255, Math.min(220, (int)(60 + 180*glow)));
            for (int i=0;i<6;i++) {
                int alpha = Math.max(8, (int)(80 * (1.0 - i/6.0) * glow));
                g2.setColor(new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), alpha));
                g2.setStroke(new BasicStroke(24 - i*3));
                g2.drawRoundRect(x-(6-i), y-(6-i), pw+(12-(i*2)), ph+(12-(i*2)), 22, 22);
            }
            g2.setColor(new Color(200,200,200,80));
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(x, y, pw, ph, 14, 14);
        }
        g2.dispose();
    }
}

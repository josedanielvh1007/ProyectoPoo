package Modelo;

import javax.swing.*;
import java.awt.*;

/*
 Painter.java
 Small utility collection for painting gradients, gloss and keys.
*/

public class Painter {

    // Background panel with vertical gradient
    public static class GradientBackground extends JPanel {
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = smooth(g);
            int w = getWidth(), h = getHeight();
            g2.setPaint(new GradientPaint(0,0,new Color(64,64,64), 0,h,new Color(44,44,44)));
            g2.fillRect(0,0,w,h);
            g2.dispose();
        }
    }

    public static Graphics2D smooth(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return g2;
    }

    // Draw white key with gloss and label
    public static void drawWhiteKey(Graphics2D g2, int w, int h, String label, boolean pressed) {
        GradientPaint gp = new GradientPaint(0,0,new Color(245,245,245), 0,h,new Color(220,220,220));
        g2.setPaint(gp);
        g2.fillRoundRect(0,0,w,h,6,6);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
        g2.setPaint(new GradientPaint(0,0,new Color(255,255,255,220), 0,h/3,new Color(255,255,255,0)));
        g2.fillRoundRect(2,2,w-4,h/3,6,6);
        g2.setComposite(AlphaComposite.SrcOver);

        g2.setColor(new Color(80,80,80,120));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0,0,w-1,h-1,6,6);

        if (pressed) {
            g2.setColor(new Color(0,150,255,40));
            g2.fillRoundRect(0,0,w,h,6,6);
        }

        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        int tx = (w - fm.stringWidth(label)) / 2;
        int ty = h - 12;
        g2.drawString(label, tx, ty);
    }

    // Draw black key with gloss
    public static void drawBlackKey(Graphics2D g2, int w, int h, boolean pressed) {
        GradientPaint gp = new GradientPaint(0,0,new Color(18,18,18), 0,h,new Color(48,48,48));
        g2.setPaint(gp);
        g2.fillRoundRect(0,0,w,h,6,6);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
        g2.setPaint(new GradientPaint(0,0,new Color(255,255,255,120), 0,h/2,new Color(255,255,255,0)));
        g2.fillRoundRect(2,2,w-4,h/2,6,6);
        g2.setComposite(AlphaComposite.SrcOver);

        if (pressed) {
            g2.setColor(new Color(255,255,255,25));
            g2.fillRoundRect(0,0,w,h,6,6);
        }
    }
}

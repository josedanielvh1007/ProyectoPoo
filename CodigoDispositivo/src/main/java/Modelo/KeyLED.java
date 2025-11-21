package Modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * KeyLED - robust LED component
 * - constructors: KeyLED(int size) and KeyLED(Color color, int size)
 * - methods: flash(), turnOn(), turnOff(), setOn(boolean)
 * - auto-off timer prevents stuck LEDs
 */
public class KeyLED extends JComponent {
    private volatile boolean on = false;
    private Color color = new Color(0, 200, 100);
    private Timer offTimer;
    private final int preferredSize;

    // Backwards-compatible constructor (size)
    public KeyLED(int size) {
        this.preferredSize = Math.max(8, size);
        setOpaque(false);
    }

    // Optional constructor with color + size
    public KeyLED(Color color, int size) {
        this.preferredSize = Math.max(8, size);
        if (color != null) this.color = color;
        setOpaque(false);
    }

    // turn on immediately
    public synchronized void turnOn() {
        on = true;
        // stop any pending off timer (LED stays on until turned off)
        if (offTimer != null && offTimer.isRunning()) {
            offTimer.stop();
            offTimer = null;
        }
        repaint();
    }

    // turn off immediately
    public synchronized void turnOff() {
        on = false;
        if (offTimer != null && offTimer.isRunning()) {
            offTimer.stop();
            offTimer = null;
        }
        repaint();
    }

    // convenience for old code that used setOn(boolean)
    public void setOn(boolean state) {
        if (state) turnOn();
        else turnOff();
    }

    // flash: turn on and schedule an automatic turn-off after `ms` milliseconds
    public synchronized void flash() {
        // default flash time short so UI feels responsive
        flash(120);
    }

    public synchronized void flash(int ms) {
        turnOn();
        // cancel previous timer
        if (offTimer != null && offTimer.isRunning()) {
            offTimer.stop();
        }
        offTimer = new Timer(Math.max(20, ms), e -> {
            synchronized (KeyLED.this) {
                turnOff();
                if (offTimer != null) {
                    offTimer.stop();
                    offTimer = null;
                }
            }
        });
        offTimer.setRepeats(false);
        offTimer.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(preferredSize, preferredSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // draw even when off (dimmed), but skip heavy painting if size is zero
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return;

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (on) {
                // bright radial glow
                Point2D center = new Point2D.Float(w / 2f, h / 2f);
                float radius = Math.max(w, h) * 1.2f;
                float[] dist = {0f, 1f};
                Color c1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 240);
                Color c2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 24);
                RadialGradientPaint rg = new RadialGradientPaint(center, radius, dist, new Color[]{c1, c2});
                g2.setPaint(rg);
                g2.fillOval(0, 0, w, h);
            } else {
                // dim dot when off
                g2.setColor(new Color(40, 40, 40, 160));
                g2.fillOval(0, 0, w, h);
                g2.setColor(new Color(100, 100, 100, 120));
                g2.drawOval(0, 0, w - 1, h - 1);
            }
        } finally {
            g2.dispose();
        }
    }
}

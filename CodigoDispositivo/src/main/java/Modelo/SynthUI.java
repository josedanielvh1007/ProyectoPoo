package Modelo;

// ─────────────────────────────────────────────
// SynthUI.java
// Centered Piano UI with LED indicators and glossy/faded styling
// Compatible with GloveSynth.java
// ─────────────────────────────────────────────

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.awt.RadialGradientPaint;
import java.awt.GradientPaint;

public class SynthUI extends JFrame {

    // Fixed container/piano sizes
    private static final int CONTAINER_WIDTH = 900;
    private static final int CONTAINER_HEIGHT = 450;
    private static final int PIANO_X = 50;
    private static final int PIANO_Y = 180;
    private static final int PIANO_WIDTH = 800;
    private static final int PIANO_HEIGHT = 240;

    // UI components
    private final RoundedPanel container = new RoundedPanel(CONTAINER_WIDTH, CONTAINER_HEIGHT);
    private final JPanel pianoPanel = new JPanel(null);

    // FIXED (constructor removed alignment param)
    private final JTextField lastNoteField = new JTextField("Last Note: (none)");

    private final JSlider volumeSlider = new JSlider(0, 127, 90);
    private final JToggleButton sustainToggle = new JToggleButton("Sustain Pedal");
    private final JSpinner octaveSpinner = new JSpinner(new SpinnerNumberModel(4, 0, 8, 1));

    // Keys & LEDs
    public final List<PianoKey> whiteKeys = new ArrayList<>();
    private final List<PianoKey> blackKeys = new ArrayList<>();
    private final List<KeyLED> keyLEDs = new ArrayList<>();

    // FIXED — explicit Swing Timer
    private final javax.swing.Timer borderDecayTimer;

    public SynthUI() {
        setTitle("SynthGlove");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 520);
        setLocationRelativeTo(null); 

        // Gradient background
        setContentPane(new GradientPanel());
        getContentPane().setLayout(null);

        // Add container
        container.setBounds(50, 20, CONTAINER_WIDTH, CONTAINER_HEIGHT);
        getContentPane().add(container);
        container.setLayout(null);

        // Title
        JLabel titleLabel = new JLabel("SynthGlove", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 8, CONTAINER_WIDTH, 36);
        container.add(titleLabel);

        // Last note field
        lastNoteField.setFont(new Font("SansSerif", Font.BOLD, 16));
        lastNoteField.setBounds((CONTAINER_WIDTH - 360) / 2, 50, 360, 36);
        lastNoteField.setEditable(false);
        lastNoteField.setForeground(Color.WHITE);
        lastNoteField.setBackground(new Color(0,0,0,0));
        lastNoteField.setOpaque(true);

        // FIXED alignment
        lastNoteField.setHorizontalAlignment(SwingConstants.CENTER);

        container.add(lastNoteField);

        setupControls();
        setupPiano();
        setupLEDs();

        // FIXED — explicit javax.swing.Timer
        borderDecayTimer = new javax.swing.Timer(60, e -> {
            container.decreaseGlow();
            container.repaint();
        });
        borderDecayTimer.start();

        setVisible(true);
    }

    private void setupControls() {
        JLabel volLabel = new JLabel("Volume", SwingConstants.CENTER);
        volLabel.setForeground(Color.WHITE);
        volLabel.setBounds(40, 100, 100, 20);
        container.add(volLabel);

        volumeSlider.setBounds(20, 125, 140, 40);
        volumeSlider.addChangeListener(e -> GloveSynth.setVolume(volumeSlider.getValue()));
        container.add(volumeSlider);

        JLabel octLabel = new JLabel("Octave", SwingConstants.CENTER);
        octLabel.setForeground(Color.WHITE);
        octLabel.setBounds((CONTAINER_WIDTH/2)-80, 100, 160, 20);
        container.add(octLabel);

        octaveSpinner.setBounds((CONTAINER_WIDTH/2)-30, 125, 60, 36);
        octaveSpinner.addChangeListener(e -> GloveSynth.setOctave((int) octaveSpinner.getValue()));
        container.add(octaveSpinner);

        sustainToggle.setBounds(CONTAINER_WIDTH - 170, 120, 140, 40);
        sustainToggle.addItemListener(e -> GloveSynth.setSustain(sustainToggle.isSelected()));
        container.add(sustainToggle);

        GloveSynth.setVolume(volumeSlider.getValue());
        GloveSynth.setOctave((int) octaveSpinner.getValue());
        GloveSynth.setSustain(sustainToggle.isSelected());
    }

    private void setupPiano() {
        pianoPanel.setOpaque(false);
        pianoPanel.setBounds(PIANO_X, PIANO_Y, PIANO_WIDTH, PIANO_HEIGHT);
        container.add(pianoPanel);

        container.setPianoBounds(pianoPanel.getBounds());

        int whiteW = PIANO_WIDTH / 7;
        int whiteH = PIANO_HEIGHT;
        int[] whiteOffsets = {0, 2, 4, 5, 7, 9, 11};
        String[] whiteNames = {"C", "D", "E", "F", "G", "A", "B"};

        for (int i = 0; i < 7; i++) {
            PianoKey w = new PianoKey(whiteNames[i], whiteOffsets[i], false);
            w.setBounds(i * whiteW, 0, whiteW, whiteH);
            pianoPanel.add(w);
            whiteKeys.add(w);
        }

        int[] blackOffsets = {1, 3, 6, 8, 10};
        String[] blackNames = {"C#", "D#", "F#", "G#", "A#"};
        int[] blackIndices = {0,1,3,4,5};

        int blackW = (int)(whiteW * 0.65);
        int blackH = (int)(whiteH * 0.62);

        for (int i = 0; i < blackOffsets.length; i++) {
            int idx = blackIndices[i];
            int x = (int)(idx * whiteW + whiteW - blackW/2.0);
            PianoKey b = new PianoKey(blackNames[i], blackOffsets[i], true);
            b.setBounds(x, 0, blackW, blackH);
            pianoPanel.add(b);
            pianoPanel.setComponentZOrder(b, 0);
            blackKeys.add(b);
        }
    }

    private void setupLEDs() {
        for (PianoKey pk : whiteKeys) {
            KeyLED led = new KeyLED(16);
            int lx = pk.getX() + (pk.getWidth()/2) - 8;
            int ly = -22;
            led.setBounds(lx, ly, 16, 16);
            pianoPanel.add(led);
            keyLEDs.add(led);
            pk.setLed(led);
        }

        for (PianoKey bk : blackKeys) {
            KeyLED led = new KeyLED(12);
            int lx = bk.getX() + (bk.getWidth()/2) - 6;
            int ly = -28;
            led.setBounds(lx, ly, 12, 12);
            pianoPanel.add(led);
            keyLEDs.add(led);
            bk.setLed(led);
        }
    }

    void setLastNote(String name) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // ───── PianoKey Component ─────
    private class PianoKey extends JComponent {
        final String name;
        final boolean black;
        private boolean pressed = false;
        private KeyLED led;

        PianoKey(String name, int semitoneOffset, boolean black) {
            this.name = name;
            this.black = black;
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    setLED(true);
                    GloveSynth.playNote(semitoneOffset, black);
                    lastNoteField.setText("Last Note: " + name);
                    container.increaseGlow();
                    repaint();
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    setLED(false);
                    GloveSynth.stopNote(semitoneOffset, black);
                    repaint();
                }
            });
        }

        void setLed(KeyLED led) { this.led = led; }
        void setLED(boolean on) { if (led != null) led.setOn(on); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (black) {
                GradientPaint gp = new GradientPaint(0, 0, new Color(18,18,18),
                        0, getHeight(), new Color(48,48,48));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
                GradientPaint gloss = new GradientPaint(0, 0, new Color(255,255,255,120),
                        0, getHeight()/2, new Color(255,255,255,0));
                g2.setPaint(gloss);
                g2.fillRoundRect(2, 2, getWidth()-4, getHeight()/2, 6, 6);
                g2.setComposite(AlphaComposite.SrcOver);

                if (pressed) {
                    g2.setColor(new Color(255,255,255,25));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                }
            } else {
                GradientPaint gp = new GradientPaint(0, 0, new Color(245,245,245),
                        0, getHeight(), new Color(220,220,220));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
                GradientPaint gloss = new GradientPaint(0, 0, new Color(255,255,255,220),
                        0, getHeight()/3, new Color(255,255,255,0));
                g2.setPaint(gloss);
                g2.fillRoundRect(2, 2, getWidth()-4, getHeight()/3, 6, 6);
                g2.setComposite(AlphaComposite.SrcOver);

                g2.setColor(new Color(80,80,80,120));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);

                if (pressed) {
                    g2.setColor(new Color(0,150,255,40));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                }

                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(name)) / 2;
                int ty = getHeight() - 12;
                g2.drawString(name, tx, ty);
            }

            g2.dispose();
        }
    }

    // ───── LED Indicator ─────
    private static class KeyLED extends JComponent {
        private boolean on = false;
        KeyLED(int size) { setOpaque(false); }

        void setOn(boolean on) { this.on = on; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (on) {
                float radius = Math.max(getWidth(), getHeight());
                Point2D center = new Point2D.Float(getWidth()/2f, getHeight()/2f);
                float[] dist = {0f, 1f};
                Color[] colors = {new Color(0,200,100,220), new Color(0,200,100,20)};
                RadialGradientPaint rg = new RadialGradientPaint(center, radius, dist, colors);
                g2.setPaint(rg);
                g2.fillOval(0, 0, getWidth(), getHeight());
            } else {
                g2.setColor(new Color(40,40,40,160));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(100,100,100,100));
                g2.drawOval(0, 0, getWidth()-1, getHeight()-1);
            }

            g2.dispose();
        }
    }

    // ───── Gradient background ─────
    private static class GradientPanel extends JPanel {
        GradientPanel() { setOpaque(true); }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            int w = getWidth(), h = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, new Color(64,64,64),
                    0, h, new Color(44,44,44));
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
            g2.dispose();
        }
    }

    // ───── Container panel ─────
    private static class RoundedPanel extends JPanel {
        private float glow = 0f;
        private Rectangle pianoBounds;
        private final int width, height;

        RoundedPanel(int w, int h) {
            this.width = w; this.height = h;
            setOpaque(false);
            setBounds(50, 20, w, h);
        }

        void setPianoBounds(Rectangle b) { pianoBounds = b; }
        void increaseGlow() { glow = Math.min(1.0f, glow + 0.25f); repaint(); }
        void decreaseGlow() { glow = Math.max(0f, glow - 0.02f); }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();

            RoundRectangle2D.Float r = new RoundRectangle2D.Float(0, 0, width-1, height-1, 20, 20);
            g2.setColor(new Color(0,0,0,30));
            g2.fill(r);

            if (pianoBounds != null) {
                int x = pianoBounds.x, y = pianoBounds.y, pw = pianoBounds.width, ph = pianoBounds.height;
                Color glowColor = new Color(0, 180, 255, Math.min(220, (int)(60 + 180*glow)));

                for (int i = 0; i < 6; i++) {
                    int alpha = Math.max(8, (int)(80 * (1.0 - i/6.0f) * glow));
                    g2.setColor(new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), alpha));
                    g2.setStroke(new BasicStroke(24 - i*3));
                    g2.drawRoundRect(x - (6-i), y - (6-i),
                            pw + (12 - (i*2)), ph + (12 - (i*2)), 22, 22);
                }

                g2.setColor(new Color(200,200,200,80));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(x, y, pw, ph, 14, 14);
            }

            g2.dispose();
        }
    }
    
    

    // ───── Main ─────
    public static void main(String[] args) {
        new Thread(() -> {
            try { GloveSynth.initMidi(); }
            catch (Exception ex) { ex.printStackTrace(); }
        }).start();

        SwingUtilities.invokeLater(SynthUI::new);
    }
}

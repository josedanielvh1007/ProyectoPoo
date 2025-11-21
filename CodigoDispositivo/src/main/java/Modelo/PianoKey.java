package Modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * PianoKey - lightweight key component (white or black) - expects
 * KeyLayouts.KeySpec style info OR simple name/semitone/black constructor -
 * uses KeyLED.flash() on press and KeyLED.turnOff() on release - not tied to a
 * large parent API (calls GloveSynth directly)
 */
public class PianoKey extends JComponent {

    private final String name;
    private final int semitone;
    private final boolean black;
    private boolean pressed = false;
    private KeyLED led;

    // constructor matching KeyLayouts.KeySpec style
    public PianoKey(KeyLayouts.KeySpec spec) {
        this(spec.name, spec.semitone, spec.black);
    }

    public PianoKey(String name, int semitone, boolean black) {
        this.name = name;
        this.semitone = semitone;
        this.black = black;
        setOpaque(false);
        initMouse();
    }

    private void initMouse() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                if (led != null) {
                    led.flash();        // flash (auto-off)
                }
                GloveSynth.playNote(semitone, black);
                // update last note if parent provides setter
                Container top = getTopLevelAncestor();
                if (top instanceof SynthUI) {
                    ((SynthUI) top).setLastNote(name);
                }
                // increase glow if container supports it
                Container p = getParent();
                if (p != null) {
                    Container gp = p.getParent();
                    if (gp instanceof RoundedPanel) {
                        ((RoundedPanel) gp).increaseGlow();
                    }
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                if (led != null) {
                    led.turnOff();    // ensure LED off on release
                }
                GloveSynth.stopNote(semitone, black);
                repaint();
            }
        });
    }

    public void setLed(KeyLED led) {
        this.led = led;
    }

    public void flashLed() {
        if (led != null) {
            led.flash();
        }
    }

    public KeyLED getLed() {
        return led;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Painter.smooth(g);
        try {
            if (black) {
                Painter.drawBlackKey(g2, getWidth(), getHeight(), pressed);
            } else {
                Painter.drawWhiteKey(g2, getWidth(), getHeight(), name, pressed);
            }
        } finally {
            g2.dispose();
        }
    }
}

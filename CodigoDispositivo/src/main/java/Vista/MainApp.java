package Vista;

import javax.swing.SwingUtilities;
import Modelo.*;

public class MainApp {

    public static void main(String[] args) {

        Persistency persistency = new Persistency("notes_log.txt");

        try {
            GloveSynth.initMidi();  // init MIDI engine
        } catch (Exception e) {
            e.printStackTrace();
        }

        SensorData sensorData = new SensorData();

        // Start MQTT listener (ESP32)
        MqttReceiver receiver = new MqttReceiver(
                "tcp://192.168.43.155:1883",
                "LaptopClient",
                sensorData
        );

        // Launch piano GUI on Swing thread
        SwingUtilities.invokeLater(() -> {
            SynthUI ui = new SynthUI();

            // Timer to update UI & synth based on sensorData
            new javax.swing.Timer(50, ev -> {
                try {
                    // ----- 1. Map pitch to volume -----
                    int volume = Math.min(127, Math.max(0, (int) ((sensorData.getPitch() + 90) / 180.0 * 127)));
                    GloveSynth.setVolume(volume);

                    // ----- 2. Map vertical displacement to octave -----
                    int octave = Math.min(8, Math.max(0, (int) ((sensorData.getDy() + 10) / 20.0 * 8)));
                    GloveSynth.setOctave(octave);

                    // ----- 3. Map roll to key index -----
                    java.lang.reflect.Field whiteKeysField = ui.getClass().getField("whiteKeys");
                    Object whiteKeysObj = whiteKeysField.get(ui);

                    if (whiteKeysObj instanceof java.util.List whiteKeysList && !whiteKeysList.isEmpty()) {
                        double normalized = (sensorData.getRoll() + 90) / 180.0; // 0..1
                        int keyIndex = (int) (normalized * (whiteKeysList.size() - 1));
                        keyIndex = Math.max(0, Math.min(whiteKeysList.size() - 1, keyIndex));

                        Object key = whiteKeysList.get(keyIndex);

                        if (sensorData.isPressed()) {
                            // Flash LED
                            java.lang.reflect.Method getLed = key.getClass().getMethod("getLed");
                            Object led = getLed.invoke(key);
                            java.lang.reflect.Method flash = led.getClass().getMethod("flash");
                            flash.invoke(led);

                            // Play note
                            java.lang.reflect.Method playNote = GloveSynth.class.getMethod("playNote", int.class, boolean.class);
                            playNote.invoke(null, keyIndex, false);

                            // Log note dynamically
                            String noteName = WHITE_KEY_NAMES[keyIndex % WHITE_KEY_NAMES.length];
                            persistency.update(noteName, octave, volume);

                        } else {
                            // Stop note when not pressed
                            java.lang.reflect.Method stopNote = GloveSynth.class.getMethod("stopNote", int.class, boolean.class);
                            stopNote.invoke(null, keyIndex, false);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();
        });
    }

    private static final String[] WHITE_KEY_NAMES = {
        "C", "D", "E", "F", "G", "A", "B"
    };
}

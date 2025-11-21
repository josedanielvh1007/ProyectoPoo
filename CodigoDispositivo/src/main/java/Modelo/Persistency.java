package Modelo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Persistency class: logs played notes to a text file.
 * Call update(noteName, octave, velocity) whenever a note is played.
 */
public class Persistency {

    private final String filename;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor
     * @param filename path to the log file (e.g., "notes_log.txt")
     */
    public Persistency(String filename) {
        this.filename = filename;
    }

    /**
     * Record a played note.
     * @param noteName Name of the note (e.g., "C", "D#")
     * @param octave Octave number
     * @param velocity MIDI velocity (0-127)
     */
    public synchronized void update(String noteName, int octave, int velocity) {
        String timestamp = LocalDateTime.now().format(formatter);
        String line = String.format("%s - Note: %s%d | Velocity: %d", timestamp, noteName, octave, velocity);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

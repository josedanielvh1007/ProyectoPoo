package Modelo;
// ─────────────────────────────────────────────
// GloveSynth.java
// MIDI Synth Engine for the Piano UI
// Methods provided for UI to call:
//   setVolume(int), setOctave(int), setSustain(boolean),
//   playNote(int semitoneOffsetFromC, boolean isSharp),
//   stopNote(int semitoneOffsetFromC, boolean isSharp)
// ─────────────────────────────────────────────

import javax.sound.midi.*;

public class GloveSynth {

    private static Synthesizer synth;
    private static MidiChannel channel;
    private static int currentVolume = 90; // 0 - 127
    private static int currentOctave = 4;  // 0 - 8 (C0 ... C8)
    private static boolean sustain = false;

    // Keep track of playing notes so we can stop them cleanly
    // index by MIDI note number
    private static final boolean[] playing = new boolean[128];

    public static void main(String[] args) throws Exception {
        initMidi();
        System.out.println("GloveSynth MIDI ready.");
        // keep thread alive if launched standalone
        while (true) Thread.sleep(1000);
    }

    public static void initMidi() throws MidiUnavailableException {
        if (synth != null && synth.isOpen()) return;
        synth = MidiSystem.getSynthesizer();
        synth.open();
        channel = synth.getChannels()[0];
        // set a pleasant instrument (optional) - 0 = Acoustic Grand Piano
        Instrument[] instruments = synth.getDefaultSoundbank().getInstruments();
        if (instruments.length > 0) synth.loadInstrument(instruments[0]);
    }

    // ───── API ─────

    // volume 0..127
    public static void setVolume(int v) {
        currentVolume = Math.max(0, Math.min(127, v));
    }

    // octave 0..8 ; 4 is middle
    public static void setOctave(int octave) {
        currentOctave = Math.max(0, Math.min(8, octave));
    }

    // sustain pedal (MIDI CC 64)
    public static void setSustain(boolean state) {
        sustain = state;
        if (channel != null) channel.controlChange(64, state ? 127 : 0);
    }

    // semitoneOffsetFromC = 0..11 relative to C of the octave (e.g. C=0, C#=1, D=2, ...)
    // isSharp is optional helper; not used for calculation but kept for compatibility
    public static synchronized void playNote(int semitoneOffsetFromC, boolean isSharp) {
        try {
            initMidi();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            return;
        }
        int midiC = 12 * (currentOctave + 1); // C4 -> 12*(4+1)=60
        int midiNote = midiC + (semitoneOffsetFromC % 12);
        if (midiNote < 0 || midiNote > 127) return;
        channel.noteOn(midiNote, currentVolume);
        playing[midiNote] = true;
    }

    public static synchronized void stopNote(int semitoneOffsetFromC, boolean isSharp) {
        if (sustain) return; // if sustain pedal down, ignore stops
        int midiC = 12 * (currentOctave + 1);
        int midiNote = midiC + (semitoneOffsetFromC % 12);
        if (midiNote < 0 || midiNote > 127) return;
        channel.noteOff(midiNote);
        playing[midiNote] = false;
    }

    // Optional: stop all (useful when closing)
    public static synchronized void allOff() {
        if (channel != null) channel.allNotesOff();
        for (int i = 0; i < playing.length; i++) playing[i] = false;
    }
}

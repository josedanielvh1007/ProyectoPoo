package Modelo;

import java.util.*;

/*
 KeyLayouts.java
 Simple key specification holder used by SynthUI and PianoKey.
*/

public class KeyLayouts {

    // KeySpec: name, semitoneOffsetFromC (0..11), visual index (white-slot index)
    public static final class KeySpec {
        public final String name;
        public final int semitone;
        public final int index;
        public final boolean black;
        public KeySpec(String name, int semitone, int index, boolean black) {
            this.name = name; this.semitone = semitone; this.index = index; this.black = black;
        }
    }

    public static KeySpec[] WHITE = {
        new KeySpec("C", 0, 0, false),
        new KeySpec("D", 2, 1, false),
        new KeySpec("E", 4, 2, false),
        new KeySpec("F", 5, 3, false),
        new KeySpec("G", 7, 4, false),
        new KeySpec("A", 9, 5, false),
        new KeySpec("B",11, 6, false)
    };

    // black keys mapped to nearest white-slot index for placement
    public static KeySpec[] BLACK = {
        new KeySpec("C#",1,0,true),
        new KeySpec("D#",3,1,true),
        new KeySpec("F#",6,3,true),
        new KeySpec("G#",8,4,true),
        new KeySpec("A#",10,5,true)
    };

    public static List<KeySpec> whiteSpecs() { return Arrays.asList(WHITE); }
    public static List<KeySpec> blackSpecs() { return Arrays.asList(BLACK); }
}

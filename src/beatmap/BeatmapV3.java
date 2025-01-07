package beatmap;

public class BeatmapV3 {
    public BeatmapV3() {}
    /**
     * Convert a BeatmapV2 map to BeatmapV3
     * @param beatmapV2
     */
    public BeatmapV3(BeatmapV2 beatmapV2) {
        // TODO
    }

    /**
     * Convert a BeatmapV2_6 map to BeatmapV3
     * @param beatmapV2_6
     */
    public BeatmapV3(BeatmapV2_6 beatmapV2_6) {
        // TODO
    }

    public String toString() {
        return String.format("BeatmapV3: (%s) [Notes:%d, Bombs:%d, Obstacles:%d, Arcs:%d, Chains:%d]",
        version, 
        colorNotes.length,
        bombNotes.length, 
        obstacles.length,
        sliders.length,
        burstSliders.length);
    }

    public static abstract class ColoredObject {
        int c; // Color
    }

    public static class ColorNote extends ColoredObject {
        public float b; // Beat
        public int x; // Line Index
        public int y; // Line Layer
        public int c; // Color
        public int d; // Cut Direction
        public int a; // Angle Offset
    }
    public static class Bomb {
        public float b; // Beat
        public int x; // Line Index
        public int y; // Line Layer
    }
    public static class Arc extends ColoredObject{
        public int c; // Color
        public float b; // Head Beat
        public int x; // Head Line Index
        public int y; // Head Line Layer
        public int d; // Head Cut Direction
        public float mu; // Head Control Point Length Multiplier
        public float tb; // Tail Beat
        public int tx; // Tail Line Index
        public int ty; // Tail Line Layer
        public int tc; // Tail Cut Direction
        public float tmu; // Tail Control Point Length Multiplier
        public int m; // Mid-Anchor Mode
    }   
    public static class Chain extends ColoredObject {
        public int c; // Color
        public float b; // Head Beat
        public int x; // Head Line Index
        public int y; // Head Line Layer
        public int d; // Head Cut Direction
        public float tb; // Tail Beat
        public int tx; // Tail Line Index
        public int ty; // Tail Line Layer
        public int sc; // Slice Count
        public float s; // Squish Factor
    }
    public static class Obstacle {
        public float b; // Beat
        public float d; // Duration
        public int x; // Line Index
        public int y; // Line Layer
        public int w; // Width
        public int h; // Height
    }
    public static class BpmEvent {
        public float b; // Beat
        public float m; // BPM
    }

    public String version;
    public BpmEvent[] bpmEvents;
    public ColorNote[] colorNotes;
    public Bomb[] bombNotes;
    public Obstacle[] obstacles;
    public Arc[] sliders;
    public Chain[] burstSliders;

}

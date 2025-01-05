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

    public static class ColorNote {
        float b; // Beat
        int x; // Line Index
        int y; // Line Layer
        int c; // Color
        int d; // Cut Direction
        int a; // Angle Offset
    }
    public static class Bomb {
        float b; // Beat
        int x; // Line Index
        int y; // Line Layer
    }
    public static class Arc {
        int c; // Color
        float b; // Head Beat
        int x; // Head Line Index
        int y; // Head Line Layer
        int d; // Head Cut Direction
        float mu; // Head Control Point Length Multiplier
        float tb; // Tail Beat
        int tx; // Tail Line Index
        int ty; // Tail Line Layer
        int tc; // Tail Cut Direction
        float tmu; // Tail Control Point Length Multiplier
        int m; // Mid-Anchor Mode
    }   
    public static class Chain {
        int c; // Color
        float b; // Head Beat
        int x; // Head Line Index
        int y; // Head Line Layer
        int d; // Head Cut Direction
        float tb; // Tail Beat
        int tx; // Tail Line Index
        int ty; // Tail Line Layer
        int sc; // Slice Count
        float s; // Squish Factor
    }
    public static class Obstacle {
        float b; // Beat
        float d; // Duration
        int x; // Line Index
        int y; // Line Layer
        int w; // Width
        int h; // Height
    }
    public static class BpmEvent {
        float b; // Beat
        float m; // BPM
    }

    public String version;
    public BpmEvent[] bpmEvents;
    public ColorNote[] colorNotes;
    public Bomb[] bombNotes;
    public Obstacle[] obstacles;
    public Arc[] sliders;
    public Chain[] burstSliders;

}

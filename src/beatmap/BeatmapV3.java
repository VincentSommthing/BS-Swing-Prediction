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

    private static final int[] MIRROR_DIRECTIONS = {0, 1, 3, 2, 5, 4, 7, 6, 8};

    public static abstract class BeatObject {
        public float b; // Head Beat
    }
    public static abstract class PositionedObject extends BeatObject {
        public int x; // Line Index
        public int y; // Line Layer
        public void mirror() {
            x = 3 - x;
        }
    }
    public static abstract class ColoredObject extends PositionedObject {
        public int c; // Color
        public int d; // Head Cut Direction
        public void mirror() {
            super.mirror();
            c = 1 - c;
            d = MIRROR_DIRECTIONS[d];
        }
    }
    public static abstract class Slider extends ColoredObject {
        public int tb; // Tail Beat
        public int tx; // Tail Line Index
        public int ty; // Tail Line Layer
        public void mirror() {
            super.mirror();
            tx = 3 - tx;
        }
    }

    public static class ColorNote extends ColoredObject {
        public int a; // Angle Offset
    }
    public static class Bomb extends PositionedObject {}
    public static class Arc extends Slider {
        public float mu; // Head Control Point Length Multiplier
        public int tc; // Tail Cut Direction
        public float tmu; // Tail Control Point Length Multiplier
        public int m; // Mid-Anchor Mode
        public void mirror() {
            super.mirror();
            tc = MIRROR_DIRECTIONS[tc];
        }
    }   
    public static class Chain extends Slider {
        public int sc; // Slice Count
        public float s; // Squish Factor
    }
    public static class Obstacle extends PositionedObject{
        public float d; // Duration
        public int w; // Width
        public int h; // Height
    }
    public static class BpmEvent extends BeatObject{
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

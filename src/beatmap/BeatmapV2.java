package beatmap;

public class BeatmapV2 {
    public static class Note {
        public double _time;
        public int _lineIndex;
        public int _lineLayer;
        public int _type;
        public int _cutDirection;
    }
    public static class Obstacle {
        public int type;
        public double _time;
        public double _duration;
        public int _lineIndex;
        public int width;
    }
    public static class Event {
        public double _time;
        public int _type;
        public double _floatValue;
    }

    public String _version;
    public Note[] _notes;
    public Obstacle[] obstacles;
}

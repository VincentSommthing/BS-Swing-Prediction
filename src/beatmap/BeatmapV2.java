package beatmap;

public class BeatmapV2 {
    public static class Note {
        public float _time;
        public int _lineIndex;
        public int _lineLayer;
        public int _type;
        public int _cutDirection;
    }
    public static class Obstacle {
        public int type;
        public float _time;
        public float _duration;
        public int _lineIndex;
        public int width;
    }

    public String _version;
    public Note[] _notes;
    public Obstacle[] obstacles;
}

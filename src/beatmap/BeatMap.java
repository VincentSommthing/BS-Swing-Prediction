package beatmap;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Beatmap {
    public Beatmap(ObjectMapper json) {

    }

    public Note[] notes;
    public Arc[] arcs;
    public Chain[] chains;
    public Bomb[] bombs;
}
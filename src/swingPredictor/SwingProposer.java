package swingEngine;

import beatmap.Note;
import beatmap.Arc;
import beatmap.Chain;
import beatmap.Bomb;


public interface SwingProposer {
    public Swing[] propose(Note note, Arc arc, Chain chain, Bomb[] bombs);
}

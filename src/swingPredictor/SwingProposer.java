package swingPredictor;

import beatmap.Note;
import beatmap.Arc;
import beatmap.Chain;
import beatmap.Bomb;

/**
 * For each note, arc, chain, and group of bombs, the predictor proposes
 * a list of Swings by running the approriate propose method.
 */
public interface SwingProposer {
    public Swing[] proposeNote(Note note);
    public Swing[] proposeArc(Arc arc);
    public Swing[] proposeChain(Chain chain);
    public Swing[] proposeBombs(Bomb[] bombs);
}

package swingPredictor;

import beatmap.BeatmapV3;

/**
 * For each note, arc, chain, and group of bombs, the predictor proposes
 * a list of Swings by running the approriate propose method.
 */
public interface SwingProposer {
    public Swing[] proposeNote(BeatmapV3.ColorNote note);
    public Swing[] proposeArc(BeatmapV3.Arc arc);
    public Swing[] proposeChain(BeatmapV3.Chain chain);
    public Swing[] proposeBombs(BeatmapV3.Bomb[] bombs);
}

package swingPredictor;

import beatmap.BeatmapV3;

/**
 * For each note, arc, chain, and group of bombs, the predictor proposes
 * a list of Swings by running the approriate propose method.
 * All notes, arcs, and chains can be assumed to be right-handed.
 * @param <T> Swing type
 */
public interface SwingProposer<T extends Swing> {
    /**
     * Propose a list of Swings for a given note
     * @param note
     * @return list of proposed Swings
     */
    public T[] proposeNote(BeatmapV3.ColorNote note);

    /**
     * Propose a list of Swings for a given arc
     * @param arc
     * @return list of proposed Swings
     */
    public T[] proposeArc(BeatmapV3.Arc arc);

    /**
     * Propose a list of Swings for a given chain
     * @param chain
     * @return list of proposed Swings
     */
    public T[] proposeChain(BeatmapV3.Chain chain);

    /**
     * Propose a list of Swings for a given group of bombs
     * @param bombs
     * @return list of proposed Swings
     */
    public T[] proposeBombs(BeatmapV3.Bomb[] bombs);
}

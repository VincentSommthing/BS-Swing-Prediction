package swingPredictor;

import beatmap.BeatmapV3.ColorNote;
import beatmap.BeatmapV3.Arc;
import beatmap.BeatmapV3.Chain;
import beatmap.BeatmapV3.Bomb;

import java.util.List;


/**
 * For each note, arc, chain, and group of bombs, the predictor proposes
 * a list of Swings by running the approriate propose method.
 * All notes, arcs, and chains can be assumed to be right-handed.
 * @param <T> Swing type
 */
public interface SwingProposer<T extends Swing> {
    /**
     * Propose a list of Swings for a given list of notes that occur at the same time
     * @param notes
     * @return list of proposed Swings
     */
    default List<T> proposeNote(List<ColorNote> notes, List<Bomb> bombs) {
        throw new UnsupportedOperationException("Need to implement proposeNote");
    };

    /**
     * Propose a list of swings given the current and previous notes and times
     * @param prevNotes list of previous notes
     * @param prevTime  time of previous notes
     * @param currNotes list of current notes
     * @param currTime time of current notes
     * @return
     */
    default List<T> proposeNote(
        List<ColorNote> prevNotes, float prevTime,
        List<ColorNote> currNotes, List<Bomb> currBombs, float currTime)
    {
        return this.proposeNote(currNotes, currBombs);
    }

    /**
     * Propsose a list of swings given the previous, current, and next notes and times
     * @param prevNotes list of previous notes
     * @param prevTime time of previous notes
     * @param currNotes list of current notes
     * @param currTime time of current notes
     * @param nextNotes list of next notes
     * @param nextTime time of next notes
     * @return
     */
    default List<T> proposeNote(
        List<ColorNote> prevNotes, float prevTime,
        List<ColorNote> currNotes, List<Bomb> currBombs, float currTime,
        List<ColorNote> nextNotes, float nextTime)
    {
        return this.proposeNote(prevNotes, prevTime, currNotes, currBombs, currTime);
    }

    /**
     * Propose a list of Swings for a given arc
     * @param arc
     * @return list of proposed Swings
     */
    List<T> proposeArc(List<Arc> arcs);

    /**
     * Propose a list of Swings for a given chain
     * @param chain
     * @return list of proposed Swings
     */
    List<T> proposeChain(Chain chain);

    /**
     * Propose a list of Swings for a given group of bombs
     * @param bombs
     * @return list of proposed Swings
     */
    List<T> proposeBombs(List<Bomb> bombs);
}

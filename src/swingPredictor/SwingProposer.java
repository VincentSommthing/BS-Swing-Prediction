package swingPredictor;

import beatmap.BeatmapV3.ColorNote;
import beatmap.BeatmapV3.Arc;
import beatmap.BeatmapV3.Chain;
import beatmap.BeatmapV3.Bomb;

import java.util.List;

/**
 * Proposes sets of swings
 * @param <T>
 */
public interface SwingProposer<T extends Swing> {
    /**
     * Proposes a set of swings for every set of notes, arc heads, arc tails, chains, and bombs that occur at a given time.
     * @param prevSwingsProposed List of swings that were proposed in the previous time step
     * @param notes List of notes
     * @param arcHeads List of arcs that start on the current swing
     * @param arcTails List of arcs that end on the current swing
     * @param chains List of chains start on the currrent swing
     * @param bombs List of bombs that occur during the swing
     * @param startTime Start time of the swing
     * @param endTime End time of the swing
     * @return
     */
    List<T> propose(List<T> prevSwingsProposed,
        List<ColorNote> notes,
        List<Arc> arcHeads,
        List<Arc> arcTails,
        List<Chain> chains,
        List<Bomb> bombs,
        double startTime,
        double endTime);
}

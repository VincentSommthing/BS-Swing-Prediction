package defaultSwingPredictor;

import beatmap.BeatmapV3.Arc;
import beatmap.BeatmapV3.Bomb;
import beatmap.BeatmapV3.Chain;
import beatmap.BeatmapV3.ColorNote;
import swingPredictor.SwingProposer;

import java.util.List;

/**
 * Default swing proposer
 */
public class DefaultSwingProposer implements SwingProposer<DefaultSwing> {
    @Override
    public List<DefaultSwing> propose(List<DefaultSwing> prevSwingsProposed,
        List<ColorNote> notes,
        List<Arc> arcHeads,
        List<Arc> arcTails,
        List<Chain> chains,
        List<Bomb> bombs,
        float startTime,
        float endTime)
    {
        // TODO
        return null;
    }
}

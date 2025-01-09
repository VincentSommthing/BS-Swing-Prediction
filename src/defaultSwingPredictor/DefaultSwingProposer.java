package defaultSwingPredictor;

import beatmap.BeatmapV3.Arc;
import beatmap.BeatmapV3.Bomb;
import beatmap.BeatmapV3.Chain;
import beatmap.BeatmapV3.ColorNote;
import swingPredictor.SwingProposer;

import java.util.List;
import java.util.ArrayList;

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
        double startTime,
        double endTime)
    {
        // TODO
        List<DefaultSwing> proposedSwings = new ArrayList<>();
        proposedSwings.add(new DefaultSwing(0, 0, 0, false));
        return proposedSwings;
    }
}

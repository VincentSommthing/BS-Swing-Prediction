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
    public List<DefaultSwing> proposeNote(List<ColorNote> note) {
        // TODO
        return null;
    }

    @Override
    public List<DefaultSwing> proposeArc(Arc arc) {
        // TODO
        return null;
    }

    @Override
    public List<DefaultSwing> proposeChain(Chain chain) {
        // TODO
        return null;
    }

    @Override
    public List<DefaultSwing> proposeBombs(List<Bomb> bombs) {
        // TODO
        return null;
    }
    
}

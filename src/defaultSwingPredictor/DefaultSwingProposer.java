package defaultSwingPredictor;

import beatmap.BeatmapV3.Arc;
import beatmap.BeatmapV3.Bomb;
import beatmap.BeatmapV3.Chain;
import beatmap.BeatmapV3.ColorNote;
import swingPredictor.SwingProposer;

public class DefaultSwingProposer implements SwingProposer<DefaultSwing> {

    @Override
    public DefaultSwing[] proposeNote(ColorNote note) {
        // TODO
        return null;
    }

    @Override
    public DefaultSwing[] proposeArc(Arc arc) {
        // TODO
        return null;
    }

    @Override
    public DefaultSwing[] proposeChain(Chain chain) {
        // TODO
        return null;
    }

    @Override
    public DefaultSwing[] proposeBombs(Bomb[] bombs) {
        // TODO
        return null;
    }
    
}

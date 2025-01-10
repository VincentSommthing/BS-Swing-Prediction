package defaultSwingPredictor;

import beatmap.BeatmapV3.Arc;
import utils.VecPair;

import java.util.function.Function;

/**
 * Default swing for arcs
 */
public class DefaultArcSwing extends DefaultSwing {
    public DefaultArcSwing(VecPair vecPair, boolean isForehand, Arc arc, double t0, double t1) {
        super(vecPair, vecPair, isForehand, t0, t1);
    }

    /**
     * Calculate cost of the swing by running arcSwingCost
     */
    @Override
    public double cost(
        Function<DefaultNoteSwing, Double> noteSwingCost,
        Function<DefaultArcSwing, Double> arcSwingCost,
        Function<DefaultBombSwing, Double> bombSwingCost) {

        return arcSwingCost.apply(this);
    }
}

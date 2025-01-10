package defaultSwingPredictor;

import utils.VecPair;

import java.util.function.Function;

/**
 * Default swing for notes
 */
public class DefaultNoteSwing extends DefaultSwing {
    public DefaultNoteSwing(VecPair start, VecPair end, boolean isForehand, double t0, double t1) {
        super(start, end, isForehand, t0, t1);
    }

    /**
     * Calculate cost of swing by running noteSwingCost
     */
    @Override
    public double cost(
        Function<DefaultNoteSwing, Double> noteSwingCost,
        Function<DefaultArcSwing, Double> arcSwingCost,
        Function<DefaultBombSwing, Double> bombSwingCost) {

        return noteSwingCost.apply(this);
    }
}

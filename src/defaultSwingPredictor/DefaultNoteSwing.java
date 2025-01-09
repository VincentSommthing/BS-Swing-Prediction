package defaultSwingPredictor;

import java.util.function.Function;

/**
 * Default swing for notes
 */
public class DefaultNoteSwing extends DefaultSwing {
    public DefaultNoteSwing(double x, double y, double rotation, boolean isForehand, double t0, double t1) {
        super(x, y, rotation, isForehand, t0, t1);
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

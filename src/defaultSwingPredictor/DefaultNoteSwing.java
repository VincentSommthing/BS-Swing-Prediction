package defaultSwingPredictor;

import java.util.function.Function;

/**
 * Default swing for notes
 */
public class DefaultNoteSwing extends DefaultSwing {
    public DefaultNoteSwing(double x0, double y0, double x1, double y1, double rot0, double rot1, boolean isForehand, double t0, double t1) {
        super(x0, y0, x1, y1, rot0, rot1, isForehand, t0, t1);
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

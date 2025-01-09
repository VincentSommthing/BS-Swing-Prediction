package defaultSwingPredictor;

import beatmap.BeatmapV3.Arc;

import java.util.function.Function;

/**
 * Default swing for arcs
 */
public class DefaultArcSwing extends DefaultSwing {
    Arc arc;
    public DefaultArcSwing(double x, double y, double rotation, boolean isForehand, Arc arc, double t0, double t1) {
        super(x, y, rotation, isForehand, t0, t1);
        this.arc = arc;
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

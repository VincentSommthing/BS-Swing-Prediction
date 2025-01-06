package defaultSwingPredictor;

import beatmap.BeatmapV3.Arc;

import java.util.function.Function;

/**
 * Default swing for arcs
 */
public class DefaultArcSwing extends DefaultSwing {
    Arc arc;
    public DefaultArcSwing(float x, float y, float rotation, boolean isForehand, Arc arc) {
        super(x, y, rotation, isForehand);
        this.arc = arc;
    }

    /**
     * Calculate cost of the swing by running arcSwingCost
     */
    @Override
    public float cost(
        Function<DefaultNoteSwing, Float> noteSwingCost,
        Function<DefaultArcSwing, Float> arcSwingCost,
        Function<DefaultBombSwing, Float> bombSwingCost) {

        return arcSwingCost.apply(this);
    }
}

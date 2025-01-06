package defaultSwingPredictor;

import java.util.function.Function;

/**
 * Default swing for notes
 */
public class DefaultNoteSwing extends DefaultSwing {
    public DefaultNoteSwing(float x, float y, float rotation, boolean isForehand) {
        super(x, y, rotation, isForehand);
    }

    /**
     * Calculate cost of swing by running noteSwingCost
     */
    @Override
    public float cost(
        Function<DefaultNoteSwing, Float> noteSwingCost,
        Function<DefaultArcSwing, Float> arcSwingCost,
        Function<DefaultBombSwing, Float> bombSwingCost) {

        return noteSwingCost.apply(this);
    }
}

package defaultSwingPredictor;

import java.util.function.Function;

public class DefaultArcSwing extends DefaultSwing {
    @Override
    public float cost(
        Function<DefaultNoteSwing, Float> noteSwingCost,
        Function<DefaultArcSwing, Float> arcSwingCost,
        Function<DefaultBombSwing, Float> bombSwingCost) {

        return arcSwingCost.apply(this);
    }
}

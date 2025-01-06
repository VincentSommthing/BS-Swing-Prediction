package defaultSwingPredictor;

import swingPredictor.CostFn;

import java.util.function.Function;

/**
 * Default cost function
 */
public class DefaultCostFn implements CostFn<DefaultSwing> {
    static private class NoteSwingCost implements Function<DefaultNoteSwing, Float> {
        public Float apply(DefaultNoteSwing swing) {
            // TODO
            return 0.0f;
        }
    }
    static private class ArcSwingCost implements Function<DefaultArcSwing, Float> {
        public Float apply(DefaultArcSwing swing) {
            // TODO
            return 0.0f;
        }
    }
    static private class BombSwingCost implements Function<DefaultBombSwing, Float> {
        public Float apply(DefaultBombSwing swing) {
            // TODO
            return 0.0f;
        }
    }

    NoteSwingCost noteSwingCost;
    ArcSwingCost arcSwingCost;
    BombSwingCost bombSwingCost;

    public DefaultCostFn() {
        this.noteSwingCost = new NoteSwingCost();
        this.arcSwingCost = new ArcSwingCost();
        this.arcSwingCost = new ArcSwingCost();
    }

    @Override
    public float transitionCost(DefaultSwing swing1, float time1, DefaultSwing swing2, float time2) {
        // TODO 
        return 0.0f;
    }

    @Override
    public float swingCost(DefaultSwing swing, float startTime, float endTime) {
        return swing.cost(this.noteSwingCost, this.arcSwingCost, this.bombSwingCost);
    }
    
}

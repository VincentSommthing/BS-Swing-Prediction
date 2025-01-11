package defaultSwingPredictor;

import swingPredictor.CostFn;

import java.util.function.Function;

/**
 * Default cost function
 */
public class DefaultCostFn implements CostFn<DefaultSwing> {
    static private class NoteSwingCost implements Function<DefaultNoteSwing, Double> {
        public Double apply(DefaultNoteSwing swing) {
            // TODO
            return Math.pow(swing.enterInfo.rot, 2);
        }
    }
    static private class ArcSwingCost implements Function<DefaultArcSwing, Double> {
        public Double apply(DefaultArcSwing swing) {
            // TODO
            return 0.0;
        }
    }
    static private class BombSwingCost implements Function<DefaultBombSwing, Double> {
        public Double apply(DefaultBombSwing swing) {
            // TODO
            return 0.0;
        }
    }

    NoteSwingCost noteSwingCost;
    ArcSwingCost arcSwingCost;
    BombSwingCost bombSwingCost;

    public DefaultCostFn() {
        // TODO
        this.noteSwingCost = new NoteSwingCost();
        this.arcSwingCost = new ArcSwingCost();
        this.arcSwingCost = new ArcSwingCost();
    }

    @Override
    public double transitionCost(DefaultSwing swing1, DefaultSwing swing2) {
        // TODO 
        return 0.0f;
    }

    /**
     * Calculates the cost of a swing
     */
    @Override
    public double swingCost(DefaultSwing swing) {
        return swing.cost(this.noteSwingCost, this.arcSwingCost, this.bombSwingCost);
    }
    
}

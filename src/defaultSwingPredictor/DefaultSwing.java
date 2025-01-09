package defaultSwingPredictor;

import swingPredictor.Swing;

import java.util.function.Function;

/**
 * Default swing interface
 */
public class DefaultSwing implements Swing {
    public double x;
    public double y;
    public double rotation;
    public boolean isForehand;
    public double t0; // Start time
    public double t1; // End time

    public DefaultSwing(double x, double y, double rotation, boolean isForehand, double t0, double t1) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.isForehand = isForehand;
        this.t0 = t0;
        this.t1 = t1;
    }

    /**
     * Calculate the cost of swing
     * @param noteSwingCost
     * @param arcSwingCost
     * @param bombSwingCost
     * @return
     */
    double cost(
        Function<DefaultNoteSwing, Double> noteSwingCost,
        Function<DefaultArcSwing, Double> arcSwingCost,
        Function<DefaultBombSwing, Double> bombSwingCost) {

        return 0.0f;
    }

    public void mirror() {
        this.x = 3 - x;
    }
}

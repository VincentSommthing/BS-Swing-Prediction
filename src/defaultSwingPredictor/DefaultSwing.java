package defaultSwingPredictor;

import swingPredictor.Swing;

import java.util.function.Function;

/**
 * Default swing interface
 */
public class DefaultSwing implements Swing {
    public double x0;
    public double y0;
    public double x1;
    public double y1;
    public double rot0;
    public double rot1;
    public boolean isForehand;
    public double t0; // Start time
    public double t1; // End time

    public DefaultSwing(double x0, double y0, double x1, double y1, double rot0, double rot1, boolean isForehand, double t0, double t1) {
        this.x0 = x0;
        this.y0 = y0;
        this.y1 = y1;
        this.y1 = y1;
        this.rot0 = rot0;
        this.rot1 = rot1;
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
        this.x0 = 3 - x0;
        this.x1 = 3 - x1;
    }

    @Override
    public String toString() {
        // return String.valueOf(this.t0) + " " + String.valueOf(this.t1);
        return String.valueOf(this.t1 - this.t0);
    }
}

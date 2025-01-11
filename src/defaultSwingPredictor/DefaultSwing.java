package defaultSwingPredictor;

import swingPredictor.Swing;
import utils.VecPair;

import java.util.function.Function;


/**
 * Default swing interface
 */
public class DefaultSwing extends VagueSwing {
    public boolean isForehand;
    public double t0; // Start time
    public double t1; // End time

    public DefaultSwing(VecPair start, VecPair end, boolean isForehand, double t0, double t1) {
        super(start, end);
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

    @Override
    public String toString() {
        int dir = (int) Math.round(this.enterInfo.rot / (Math.PI / 8));
        if (isForehand) {
            dir += 4;
        }
        dir = Math.floorMod(dir, 8);
        return String.valueOf("↓↘→↗↑↖←↙".charAt(dir));
        // return String.valueOf(this.enterInfo.rot);
    }

    @Override
    public boolean equals(Object other) {
        return this == other;
    }
}
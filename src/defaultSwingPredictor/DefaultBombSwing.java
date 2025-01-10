package defaultSwingPredictor;

import beatmap.BeatmapV3.Bomb;

import java.util.function.Function;

/**
 * Default swing for bombs
 */
public class DefaultBombSwing extends DefaultSwing {
    Bomb[] bombs;
    public DefaultBombSwing(double x, double y, double rot, boolean isForehand, Bomb[] bombs, double t0, double t1) {
        super(x, y, x, y, rot, rot, isForehand, t0, t1);
        this.bombs = bombs;
    }

    /**
     * Calculate cost of swing by running bombSwingCost
     */
    @Override
    public double cost(
        Function<DefaultNoteSwing, Double> noteSwingCost,
        Function<DefaultArcSwing, Double> arcSwingCost,
        Function<DefaultBombSwing, Double> bombSwingCost) {

        return bombSwingCost.apply(this);
    }
}

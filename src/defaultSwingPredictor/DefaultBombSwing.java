package defaultSwingPredictor;

import beatmap.BeatmapV3.Bomb;

import java.util.function.Function;

/**
 * Default swing for bombs
 */
public class DefaultBombSwing extends DefaultSwing {
    Bomb[] bombs;
    public DefaultBombSwing(float x, float y, float rotation, boolean isForehand, Bomb[] bombs) {
        super(x, y, rotation, isForehand);
        this.bombs = bombs;
    }

    /**
     * Calculate cost of swing by running bombSwingCost
     */
    @Override
    public float cost(
        Function<DefaultNoteSwing, Float> noteSwingCost,
        Function<DefaultArcSwing, Float> arcSwingCost,
        Function<DefaultBombSwing, Float> bombSwingCost) {

        return bombSwingCost.apply(this);
    }
}

package defaultSwingPredictor;

import swingPredictor.Swing;

import java.util.function.Function;

/**
 * Default swing interface
 */
public class DefaultSwing implements Swing {
    public float x;
    public float y;
    public float rotation;
    public boolean isForehand;

    public DefaultSwing(float x, float y, float rotation, boolean isForehand) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.isForehand = isForehand;
    }

    float cost(
        Function<DefaultNoteSwing, Float> noteSwingCost,
        Function<DefaultArcSwing, Float> arcSwingCost,
        Function<DefaultBombSwing, Float> bombSwingCost) {

        return 0.0f;
    }

    public void mirror() {
        this.x = 3 - x;
    }
}

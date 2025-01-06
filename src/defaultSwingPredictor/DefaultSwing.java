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

    float cost(Function<DefaultSwing, Float> noteSwingCost, Function<DefaultSwing, Float> arcSwingCost, Function<DefaultSwing, Float> bombSwingCost) {
        return 0.0f;
    }

    public void mirror() {
        this.x = 3 - x;
    }
}

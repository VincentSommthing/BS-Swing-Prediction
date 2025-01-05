package defaultSwingPredictor;

import swingPredictor.CostFn;

public class DefaultCostFn implements CostFn<DefaultSwing> {
    @Override
    public float transitionCost(DefaultSwing swing1, float time1, DefaultSwing swing2, float time2) {
        // TODO 
        return 0.0f;
    }

    @Override
    public float swingCost(DefaultSwing swing, float startTime, float endTime) {
        // TODO 
        return 0.0f;
    }
    
}

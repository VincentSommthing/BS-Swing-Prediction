package swingPredictor;

public interface Swing {
    /**
     * Calculate the cost of the transition from the previous swing to the current swing
     * @param prevSwing previous swing
     * @param prevTime ending time of the previous swing
     * @param currTime starting time of the current swing
     * @return cost of the transition
     */
    float transitionCost(Swing prevSwing, float prevTime, float currTime);

    /**
     * Calculate the cost of the swing.
     * @param startTime Time of the first beatmap object in the swing
     * @param endTime time of the last beatmap object in the swing
     * @return cost of the swing
     */
    float swingCost(float startTime, float endTime);
}
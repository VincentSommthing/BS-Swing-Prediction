package swingPredictor;

/**
 * Cost function to calculate cost of swings and transitions between swings.
 * All swings can be assumed to be right-handed.
 * @param <T> class of swings this cost function is for
 */
public interface CostFn<T extends Swing> {
    /**
     * Calculate the cost of the transition from the one swing to the next
     * @param swing1 first swing
     * @param time1 ending time of the first swing
     * @param swing2 next swing
     * @param time2 starting time of the next swing
     * @return cost of the transition
     */
    float transitionCost(T swing1, float time1, T swing2, float time2);

    /**
     * Calculate the cost of a swing.
     * @param swing swing to calculate cost of
     * @param startTime time of the first beatmap object in the swing
     * @param endTime time of the last beatmap object in the swing
     * @return cost of the swing
     */
    float swingCost(T swing, float startTime, float endTime);
}
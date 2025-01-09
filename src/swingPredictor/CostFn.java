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
     * @param swing2 next swing
     * @return cost of the transition
     */
    double transitionCost(T swing1, T swing2);

    /**
     * Calculate the cost of a swing.
     * @param swing swing to calculate cost of
     * @return cost of the swing
     */
    double swingCost(T swing);
}
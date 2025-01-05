package swingEngine;
public interface Swing {
    /**
     * Calculate the cost of the current swing given the previous swing
     * @param prevSwing previous swing
     * @return cost of the swing
     */
    float cost(Swing prevSwing);
}
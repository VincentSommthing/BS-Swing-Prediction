package swingPredictor;

import beatmap.BeatmapV3;

public class Predictor<T extends Swing> {
    private SwingProposer<T> proposer;
    private CostFn<T> costFn;

    /**
     * Constructor
     * @param swingProposer
     * @param costFn
     */
    public Predictor(SwingProposer<T> proposer, CostFn<T> costFn) {
        this.proposer = proposer;
        this.costFn = costFn;
    }

    /**
     * Calculates the sequence of swings that gives the lowest total cost
     * @param beatmap beatmap object
     * @return list of swings
     */
    public T[] predict(BeatmapV3 beatmap) {
        // TODO Implement predict
        return null;
    }
}

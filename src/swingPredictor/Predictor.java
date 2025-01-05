package swingPredictor;

import beatmap.BeatmapV3;

public class Predictor {
    private SwingProposer proposer;
    public Predictor(SwingProposer swingProposer) {
        proposer = swingProposer;
    }
    public Swing[] predict(BeatmapV3 beatmap) {
        // TODO Implement predict
        return new Swing[] {};
    }
}

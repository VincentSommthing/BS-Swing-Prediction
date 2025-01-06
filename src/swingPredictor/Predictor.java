package swingPredictor;

import beatmap.BeatmapV3;
import beatmap.BeatmapV3.ColorNote;
import beatmap.BeatmapV3.Arc;
import beatmap.BeatmapV3.Chain;
import beatmap.BeatmapV3.Bomb;

import java.util.List;
import java.util.ArrayList;

public class Predictor<T extends Swing> {
    /**
     * Stores a beatmap object and the beat it occurs at
     */
    private abstract class BeatPair<S> {
        protected float beat;
        protected S obj;

        public BeatPair(float beat, S obj) {
            this.beat = beat;
            this.obj = obj;
        }
    }
    /**
     * Stores a note and the beat it occurs at
     */
    private class NoteBeatPair extends BeatPair<ColorNote> {
        public NoteBeatPair(ColorNote note) {
            super(note.b, note);
        }
    }
    /**
     * Stores an arc and the beat it occurs at
     */
    private class ArcBeatPair extends BeatPair<Arc> {
        public ArcBeatPair(Arc arc) {
            super(arc.tb, arc);
        }
    }
    /**
     * Stores a chain and the beat it occurs at
     */
    private class ChainBeatPair extends BeatPair<Chain> {
        public ChainBeatPair(Chain chain) {
            super(chain.b, chain);
        }
    }
        /**
     * Stores a note and the beat it occurs at
     */
    private class BombBeatPair extends BeatPair<Bomb> {
        public BombBeatPair(Bomb bomb) {
            super(bomb.b, bomb);
        }
    }

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
    public List<T> predict(BeatmapV3 beatmap) {
        // TODO Implement predict

        // Create a list of beat pairs
        List<BeatPair<?>> beatPairs = new ArrayList<>();
        // Add all notes
        for (ColorNote note : beatmap.colorNotes) {
            beatPairs.add(new NoteBeatPair(note));
        }
        // Add all arcs
        for (Arc arc : beatmap.sliders) {
            beatPairs.add(new ArcBeatPair(arc));
        }
        // Add all chains
        for (Chain chain : beatmap.burstSliders) {
            beatPairs.add(new ChainBeatPair(chain));
        }
        // Add all bombs
        for (Bomb bomb : beatmap.bombNotes) {
            beatPairs.add(new BombBeatPair(bomb));
        }

        // Sort the beat pairs by beat
        beatPairs.sort((a, b) -> Float.compare(a.beat, b.beat));

        return null;
    }
}

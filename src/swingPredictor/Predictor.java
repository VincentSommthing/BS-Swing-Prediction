package swingPredictor;

import beatmap.BeatmapV3;
import beatmap.BeatmapV3.ColorNote;
import beatmap.BeatmapV3.Arc;
import beatmap.BeatmapV3.Chain;
import beatmap.BeatmapV3.Bomb;
import beatmap.BeatmapV3.ColoredObject;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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

    /**
     * A group of objects that occur at the same beat
     */
    private class BeatGroup<S> {
        public float beat; // Beat that the object occurs at
        public List<S> objs; // List of objects
        public BeatGroup(float beat) {
            this.beat = beat;
            this.objs = new ArrayList<>();
        }
    }
    /**
     * Groups beat pairs by beat
     * @param <S> Type of beatmap object
     * @param beatPairs List of beat pairs to group
     * @return list of beat groups
     */
    private <S> List<BeatGroup<S>> groupByBeat(List<BeatPair<S>> beatPairs) {
        // Initialize list of groups
        List<BeatGroup<S>> groups = new ArrayList<>();

        if (!beatPairs.isEmpty()) {
            // sort the beat pairs by beat
            beatPairs.sort((a, b) -> Float.compare(a.beat, b.beat));

            // create a group for the first beat object
            BeatGroup<S> currentGroup = new BeatGroup<>(beatPairs.get(0).beat);
            // iterate through all beat pairs
            for (BeatPair<S> beatPair: beatPairs) {
                // if beat does not match the current group, add the current group to the list and create a new group
                if (currentGroup.beat != beatPair.beat) {
                    groups.add(currentGroup);
                    currentGroup = new BeatGroup<>(beatPair.beat);
                }
                // add the beatmap object to the current group
                currentGroup.objs.add(beatPair.obj);
            }
        }
        return groups;
    }


    /**
     * Represents two lists, one for each color
     */
    private class ColorSeparatedList<S> {
        private List<S>[] lists;

        @SuppressWarnings("unchecked")
        public ColorSeparatedList() {
            this.lists = (ArrayList<S>[]) new ArrayList[2];
            this.lists[0] = new ArrayList<>();
            this.lists[1] = new ArrayList<>();
        }

        /**
         * Get the list corresponding to the given color
         * @param color
         * @return list
         */
        public List<S> get(int color) {
            return this.lists[color];
        }
    }
    /**
     * Separates a list of colored beatmap objects by color
     * @param <S> Type of colored beatmap object
     * @param array Array to separate
     * @return Color separated list
     */
    private <S extends ColoredObject> ColorSeparatedList<S> separateByColor(S[] array) {
        ColorSeparatedList<S> colSepList = new ColorSeparatedList<>();
        for (S obj : array) {
            colSepList.get(obj.c).add(obj);
        }
        return colSepList;
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

        // Separate colorNotes, arcs, chains by color
        ColorSeparatedList<ColorNote> colorSeparatedNotes = this.separateByColor(beatmap.colorNotes);
        ColorSeparatedList<Arc> colorSeparatedArcs = this.separateByColor(beatmap.sliders);
        ColorSeparatedList<Chain> colorSeparatedChains = this.separateByColor(beatmap.burstSliders);

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

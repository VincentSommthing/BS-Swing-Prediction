package swingPredictor;

import beatmap.BeatmapV3;
import beatmap.BeatmapV3.ColorNote;
import beatmap.BeatmapV3.Arc;
import beatmap.BeatmapV3.Chain;
import beatmap.BeatmapV3.Bomb;
import beatmap.BeatmapV3.ColorObject;
import beatmap.BeatmapV3.BeatObject;

import java.util.List;
import java.util.ArrayList;

public class Predictor<T extends Swing> {
    /**
     * Stores a beatmap object and the beat it occurs at
     */
    private static abstract class BeatPair<S extends BeatObject> {
        float beat;
        S obj;

        public BeatPair() {}
        public BeatPair(S obj) {
            this.beat = obj.b;
            this.obj = obj;
        }
    }
    /**
     * Stores a note and the beat it occurs at
     */
    private static class NoteBeatPair extends BeatPair<ColorNote> {
        public NoteBeatPair(ColorNote note) {
            super(note);
        }
    }
    /**
     * Stores an arc and the beat it occurs at
     */
    private static class ArcBeatPair extends BeatPair<Arc> {
        public ArcBeatPair (Arc arc) {
            super(arc);
        }
    }
    /**
     * Stores a chain and the beat it occurs at
     */
    private static class ChainBeatPair extends BeatPair<Chain> {
        public ChainBeatPair(Chain chain) {
            super(chain);
        }
    }
    /**
     * Stores a note and the beat it occurs at
     */
    private static class BombBeatPair extends BeatPair<Bomb> {
        public BombBeatPair(Bomb bomb) {
            super(bomb);
        }
    }

    /**
     * Represents two lists, one for each color
     */
    private class ColorSeparatedPair<S> {
        private S[] pair;

        @SuppressWarnings("unchecked")
        public ColorSeparatedPair() {
            this.pair = (S[]) new Object[2];
        }

        /**
         * Get the list corresponding to the given color
         * @param color
         * @return list
         */
        public S get(int color) {
            return this.pair[color];
        }

        /**
         * Set the list corresponding to the given color
         * @param color
         * @param list
         */
        public void set(int color, S item) {
            this.pair[color] = item;
        }
    }
    /**
     * Separates a list of colored beatmap objects by color
     * @param <S> Type of colored beatmap object
     * @param array Array to separate
     * @return Color separated list
     */
    private <S extends ColorObject> ColorSeparatedPair<List<S>> separateByColor(S[] array) {
        ColorSeparatedPair<List<S>> colSepList = new ColorSeparatedPair<>();
        for (int i = 0; i < 2; i++) {
            colSepList.set(i, new ArrayList<>());
        }
        for (S obj : array) {
            colSepList.get(obj.c).add(obj);
        }
        return colSepList;
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
     * @return List of beat groups
     */
    private <S extends BeatObject> List<BeatGroup<S>> groupByBeat(List<BeatPair<S>> beatPairs) {
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
     * Groups each list in a color-separated list by beat
     * @param <S> Type of beatmap object
     * @param colSepList Color-separated list to group
     * @return Color-separated list of beat groups
     */
    private <S extends BeatObject> ColorSeparatedPair<List<BeatGroup<S>>> groupByBeat(ColorSeparatedPair<List<BeatPair<S>>> colSepList) {
        // Initialize a color-separated list of beat groups
        ColorSeparatedPair<List<BeatGroup<S>>> colSepGroupsList = new ColorSeparatedPair<>();
        // iterate through each color
        for (int i = 0; i < 2; i++) {
            // get the beat pairs corresponding to color i
            List<BeatPair<S>> beatPairs = colSepList.get(i);
            // group the beat pairs by beat
            List<BeatGroup<S>> beatGroups = this.groupByBeat(beatPairs);
            // assign the list of beat groups to the color-separated list
            colSepGroupsList.set(i, beatGroups);
        }
        // return the list
        return colSepGroupsList;
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
        ColorSeparatedPair<List<ColorNote>> colorSeparatedNotes = this.separateByColor(beatmap.colorNotes);
        ColorSeparatedPair<List<Arc>> colorSeparatedArcs = this.separateByColor(beatmap.sliders);
        ColorSeparatedPair<List<Chain>> colorSeparatedChains = this.separateByColor(beatmap.burstSliders);

        // Group each list by beat
        // ColorSeparatedPair<List<BeatGroup<ColorNote>>> colorSeparatedNoteGroups = this.groupByBeat(colorSeparatedNotes);

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

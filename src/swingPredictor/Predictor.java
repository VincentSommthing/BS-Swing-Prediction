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
import java.util.Arrays;

public class Predictor<T extends Swing> {
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
     * Stores a beatmap object and the beat it occurs at
     */
    private static class BeatPair<S extends BeatObject> {
        float beat;
        S obj;

        public BeatPair(S obj) {
            if (obj instanceof Arc arc) {
                this.beat = arc.tb;
            } else {
                this.beat = obj.b;
            }
            this.obj = obj;
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
        public static <U> int compare(BeatGroup<U> a, BeatGroup<U> b) {
            return Float.compare(a.beat, b.beat);
        }
    }
    /**
     * Groups beat pairs by beat
     * @param <S> Type of beatmap object
     * @param beatPairs List of beat pairs to group
     * @return List of beat groups
     */
    private <S extends BeatObject> List<BeatGroup<S>> groupByBeat(Iterable<S> beatObjects) {
        // Turn into beat pairs
        List<BeatPair<S>> beatPairs = new ArrayList<>();
        for (S beatObject: beatObjects) {
            beatPairs.add(new BeatPair<>(beatObject));
        }

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
    private <S extends BeatObject> List<BeatGroup<S>> groupByBeat(S[] beatObjects) {
        return groupByBeat(Arrays.asList(beatObjects));
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
        ColorSeparatedPair<List<ColorNote>> colorSeparatedNotes = separateByColor(beatmap.colorNotes);
        ColorSeparatedPair<List<Arc>> colorSeparatedArcs = separateByColor(beatmap.sliders);
        ColorSeparatedPair<List<Chain>> colorSeparatedChains = separateByColor(beatmap.burstSliders);

        // Group bombs by beat
        List<BeatGroup<Bomb>> bombGroups = groupByBeat(beatmap.bombNotes);

        // Iterate through each color
        for (int i = 0; i < 2; i++) {
            // Isolate the beatmap objects corresponding to color i
            List<ColorNote> colorNotes = colorSeparatedNotes.get(i);
            List<Arc> arcs = colorSeparatedArcs.get(i);
            List<Chain> chains = colorSeparatedChains.get(i);

            // Group by beat
            List<BeatGroup<ColorNote>> noteGroups = groupByBeat(colorNotes);
            List<BeatGroup<Arc>> arcGroups = groupByBeat(arcs);
            List<BeatGroup<Chain>> chainGroups = groupByBeat(chains);

            // Combine all beat groups
            List<BeatGroup<?>> beatGroups = new ArrayList<>();
            beatGroups.addAll(noteGroups);
            beatGroups.addAll(arcGroups);
            beatGroups.addAll(chainGroups);
            beatGroups.addAll(bombGroups);

            // Sort by beat
            beatGroups.sort(BeatGroup::compare);
        }

        return null;
    }
}

package swingPredictor;

import beatmap.BeatmapV3;
import beatmap.BeatmapV3.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

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
    private static abstract class BeatPair<S> {
        double beat;
        S obj;
        public static <U> int compare(BeatPair<U> a, BeatPair<U> b) {
            return Double.compare(a.beat, b.beat);
        }
    }
    public static class BeatPairHead<S extends BeatObject> extends BeatPair<S> {
        public BeatPairHead(S obj) {
            this.beat = obj.b;
            this.obj = obj;
        }
    }
    private static class BeatPairTail<S extends Slider> extends BeatPair<S> {
        public BeatPairTail(S obj) {
            this.beat = obj.tb;
            this.obj = obj;
        }
    }
    /**
     * Converts a list of beat objects into a list of beat pairs
     * @param <S>
     * @param list List of beat objects to convert
     * @param fn Function that produces a beat pair given a beat object
     * @return
     */
    private <S extends BeatObject> List<BeatPair<S>> toBeatPairs(List<S> list, Function<S, BeatPair<S>> fn) {
        List<BeatPair<S>> beatPairs = new ArrayList<>();
        for (S obj : list) {
            beatPairs.add(fn.apply(obj));
        }
        return beatPairs;
    }
    private <S extends BeatObject> List<BeatPair<S>> toBeatPairs(S[] array) {
        return toBeatPairs(Arrays.asList(array));
    }
    private <S extends BeatObject> List<BeatPair<S>> toBeatPairs(List<S> list) {
        return toBeatPairs(list, BeatPairHead::new);
    }

    /**
     * A group of objects that occur at the same beat
     */
    private class BeatGroup<S> extends BeatPair<List<S>> {
        public BeatGroup(double beat) {
            this.beat = beat;
            this.obj = new ArrayList<>();
        }
        public void add(S item) {
            this.obj.add(item);
        }
    }
    /**
     * Groups beat pairs by beat
     * @param <S> Type of beatmap object
     * @param beatPairs List of beat pairs to group
     * @return List of beat groups
     */
    private <S, U extends BeatPair<S>> List<BeatGroup<S>> groupByBeat(List<U> beatPairs) {
        // Initialize list of groups
        List<BeatGroup<S>> groups = new ArrayList<>();

        if (!beatPairs.isEmpty()) {
            // sort the beat pairs by beat
            beatPairs.sort(BeatPair::compare);

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
                currentGroup.add(beatPair.obj);
            }
        }
        return groups;
    }

    private SwingProposer<T> proposer;
    private CostFn<T> costFn;
    private double bombWindowSize;

    /**
     * Constructor
     * @param swingProposer
     * @param costFn
     */
    public Predictor(SwingProposer<T> proposer, CostFn<T> costFn) {
        this(proposer, costFn, 0.2);
    }
    public Predictor(SwingProposer<T> proposer, CostFn<T> costFn, double bombWindowSize) {
        this.proposer = proposer;
        this.costFn = costFn;
        this.bombWindowSize = bombWindowSize;
    }

    /**
     * Calculates the sequence of swings that gives the lowest total cost
     * @param beatmap beatmap object
     * @return list of swings
     */
    public List<T> predict(BeatmapV3 beatmap, double bpm) {
        // TODO Implement predict
        final double SECONDSPERMINUTE = 60.0;
        final double EPS = 1e-5;

        // Separate colorNotes, arcs, chains by color
        ColorSeparatedPair<List<ColorNote>> colorSeparatedNotes = separateByColor(beatmap.colorNotes);
        ColorSeparatedPair<List<Arc>> colorSeparatedArcs = separateByColor(beatmap.sliders);
        ColorSeparatedPair<List<Chain>> colorSeparatedChains = separateByColor(beatmap.burstSliders);

        // Group bombs by beat
        List<BeatPair<Bomb>> beatPairBombs = toBeatPairs(beatmap.bombNotes);
        List<BeatGroup<Bomb>> bombGroups = groupByBeat(beatPairBombs);

        // Turn BPM changes into beat pairs
        List<BeatPair<BpmEvent>> beatPairBpms = toBeatPairs(beatmap.bpmEvents);

        // Iterate through each color
        for (int i = 0; i < 2; i++) {
            // Isolate the beatmap objects corresponding to color i
            List<ColorNote> colorNotes = colorSeparatedNotes.get(i);
            List<Arc> arcs = colorSeparatedArcs.get(i);
            List<Chain> chains = colorSeparatedChains.get(i);

            // Make everything into beat pairs
            List<BeatPair<ColorNote>> beatPairNotes = toBeatPairs(colorNotes);
            List<BeatPair<Arc>> beatPairArcHeads = toBeatPairs(arcs);
            List<BeatPair<Chain>> beatPairChains = toBeatPairs(chains);
            List<BeatPair<Arc>> beatPairArcTails = toBeatPairs(arcs, BeatPairTail::new);

            // Group by beat
            List<BeatGroup<ColorNote>> noteGroups = groupByBeat(beatPairNotes);
            List<BeatGroup<Arc>> arcHeadGroups = groupByBeat(beatPairArcHeads);
            List<BeatGroup<Chain>> chainGroups = groupByBeat(beatPairChains);
            List<BeatGroup<Arc>> arcTailGroups = groupByBeat(beatPairArcTails);

            // Combine all beat groups
            List<BeatPair<?>> beatPairs = new ArrayList<>();
            beatPairs.addAll(noteGroups);
            beatPairs.addAll(arcHeadGroups);
            beatPairs.addAll(arcTailGroups);
            beatPairs.addAll(chainGroups);
            beatPairs.addAll(bombGroups);
            beatPairs.addAll(beatPairBpms);

            // Sort
            beatPairs.sort(BeatGroup::compare);

            boolean containsNotes = false;
            double beat = 0.0;
            double time = 0.0;
            double currentStartTime = 0.0;
            double currentEndTime = 0.0;
            List<List<T>> proposedSwings = new ArrayList<>();
            // Group by beat
            for (BeatPair<?> beatPair : beatPairs) {
                // calculate time of beatGroup
                double dbeat = beatPair.beat - beat;
                double dt = dbeat / bpm * SECONDSPERMINUTE;

                beat = beatPair.beat;
                time += dt;
                
                if (beatPair.obj instanceof BpmEvent bpmEvent) {
                    // If the object is a bpm event, change the bpm
                    bpm = bpmEvent.m;
                } else {
                    if (containsNotes) {
                        if (time <= currentEndTime + EPS) {
                            
                        }
                    }
                }
            }
        }

        return null;
    }
}

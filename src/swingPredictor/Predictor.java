package swingPredictor;

import beatmap.BeatmapV3;
import beatmap.BeatmapV3.*;
import utils.Constants;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;

public class Predictor<T extends Swing> {
    final private double EPS = Constants.EPS;
    final double SECONDSPERMINUTE = 60.0; // Seconds per minute


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
    private class BeatGroup<S> extends BeatPair<List<S>> implements Iterable<S> {
        public BeatGroup(double beat) {
            this.beat = beat;
            this.obj = new ArrayList<>();
        }
        public void add(S item) {
            this.obj.add(item);
        }
        public Stream<S> stream() {
            return obj.stream();
        }
        @Override
        public Iterator<S> iterator() {
            return obj.iterator();
        }
    }
    /**
     * Groups beat pairs by beat
     * @param <S> Type of beatmap object
     * @param beatPairs List of beat pairs to group
     * @return List of beat groups
     */
    private List<BeatGroup<?>> groupByBeat(List<BeatPair<?>> beatPairs) {
        // Initialize list of groups
        List<BeatGroup<?>> groups = new ArrayList<>();

        if (!beatPairs.isEmpty()) {
            // sort the beat pairs by beat
            beatPairs.sort(BeatPair::compare);

            // create a group for the first beat object
            BeatGroup<Object> currentGroup = new BeatGroup<>(beatPairs.get(0).beat);
            // iterate through all beat pairs
            for (BeatPair<?> beatPair: beatPairs) {
                // if beat does not match the current group, add the current group to the list and create a new group
                if (beatPair.beat > currentGroup.beat + EPS) {
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
     * Proposes swings using the proposer
     * @param beatmap
     * @param bpm
     * @param beatPairBombs
     * @param beatPairBpms
     * @param colorNotes
     * @param arcs
     * @param chains
     * @return
     */
    private List<List<T>> propose(
        BeatmapV3 beatmap,
        double bpm,
        List<BeatPair<Bomb>> beatPairBombs,
        List<BeatPair<BpmEvent>> beatPairBpms,
        List<ColorNote> colorNotes,
        List<Arc> arcs,
        List<Chain> chains)
    {
        // Make everything into beat pairs
        List<BeatPair<ColorNote>> beatPairNotes = toBeatPairs(colorNotes);
        List<BeatPair<Arc>> beatPairArcHeads = toBeatPairs(arcs);
        List<BeatPair<Chain>> beatPairChains = toBeatPairs(chains);
        List<BeatPair<Arc>> beatPairArcTails = toBeatPairs(arcs, BeatPairTail::new);

        // Combine all beat groups
        List<BeatPair<?>> beatPairs = new ArrayList<>();
        beatPairs.addAll(beatPairNotes);
        beatPairs.addAll(beatPairArcHeads);
        beatPairs.addAll(beatPairChains);
        beatPairs.addAll(beatPairArcTails);
        beatPairs.addAll(beatPairBombs);
        beatPairs.addAll(beatPairBpms);

        List<BeatGroup<?>> beatGroups = groupByBeat(beatPairs);

        // Sort
        beatPairs.sort(BeatGroup::compare);

        List<List<T>> proposedSwings = new ArrayList<>();

        List<ColorNote> currNotes = null;
        List<Arc> currArcHeads = null;
        List<Arc> currArcTails = null;
        List<Chain> currChains = null;
        List<Bomb> currBombs = null;
        boolean requiresInit = true;
        boolean onlyBombs = true;
        double beat = 0.0;
        double time = 0.0;
        double currStartTime = 0.0;
        double currEndBeat = 0.0;

        // Group by beat
        for (BeatGroup<?> beatGroup : beatGroups) {
            // calculate time of beatGroup
            double dbeat = beatGroup.beat - beat;
            double dt = dbeat / bpm * SECONDSPERMINUTE;

            beat = beatGroup.beat;
            time += dt;

            // prose swings if these conditions are met
            if (!requiresInit // everything is already initialized
                && (beatGroup.stream().anyMatch(a -> a instanceof ColorNote || a instanceof Chain) // current beatGroup containts note or chain
                || (onlyBombs && time > currStartTime + bombWindowSize + EPS) // outside bomb window
                || (!onlyBombs && beat > currEndBeat + EPS)) // outside current end beat
            ) {
                List<T> prevSwings = null;
                if (!proposedSwings.isEmpty()) {
                    prevSwings = proposedSwings.getLast();
                }
                // Propose swings
                List<T> currProposedSwings = proposer.propose(
                    prevSwings,
                    currNotes,
                    currArcHeads,
                    currArcTails,
                    currChains,
                    currBombs,
                    currStartTime,
                    time - (beat - currEndBeat) / bpm * SECONDSPERMINUTE);

                if (currProposedSwings != null) {
                    proposedSwings.add(currProposedSwings);
                }

                // require initialization for next set of beat objects
                requiresInit = true;
            }

            // Initialize if required
            if (requiresInit) {
                currNotes = new ArrayList<>();
                currArcHeads = new ArrayList<>();
                currArcTails = new ArrayList<>();
                currChains = new ArrayList<>();
                currBombs = new ArrayList<>();
                currStartTime = time;
                currEndBeat = beat;
                requiresInit = false;
                onlyBombs = true;
            }
            
            // Iterate through beat grup and add everything
            for (Object obj : beatGroup) {
                if (obj instanceof BpmEvent bpmEvent) {
                    // If the object is a bpm event, change the bpm
                    bpm = bpmEvent.m;

                } else if (obj instanceof ColorNote note) {
                    currNotes.add(note);
                    onlyBombs = false;

                } else if (obj instanceof Arc arc) {
                    if (arc.tb <= currEndBeat + EPS) {
                        currArcTails.add(arc);
                    } else {
                        currArcHeads.add(arc);
                    }
                    onlyBombs = false;

                } else if (obj instanceof Chain chain) {
                    currChains.add(chain);
                    currEndBeat = Math.max(currEndBeat, chain.tb);
                    onlyBombs = false;

                } else if (obj instanceof Bomb bomb) {
                    currBombs.add(bomb);
                }
            }
            currEndBeat = Math.max(currEndBeat, beat);

        }
        return proposedSwings;
    }


    /**
     * Calculates the sequence of swings that minimzes the cost given a list of proposed swings
     * @param proposedSwings
     * @return
     */
    private List<T> predict(List<List<T>> proposedSwings) {
        // TODO
        List<T> output = new ArrayList<>();


        // If the list of proposed swings is empty, return.
        if (proposedSwings.isEmpty()) {
            return output;
        }

        Map<T, Double> costs = new HashMap<>();
        Map<T, T> bestPrevSwings = new HashMap<>();
        // Populate the cost map with the first set of swings
        for (T swing : proposedSwings.getFirst()) {
            costs.put(swing, costFn.swingCost(swing));
        }

        for (int i = 1; i < proposedSwings.size(); i++) {
            List<T> currSwings = proposedSwings.get(i);
            List<T> prevSwings = proposedSwings.get(i - 1);
            // Iterate through all current swings
            for (T currSwing : currSwings) {
                // Keep track of best cost and prev swing so far
                double bestCost = Double.POSITIVE_INFINITY;
                T bestPrevSwing = null;

                // iterate through all previous swings
                for (T prevSwing : prevSwings) {
                    // Calculate the total cost
                    double transitionCost = costFn.transitionCost(prevSwing, currSwing);
                    double swingCost = costFn.swingCost(currSwing);
                    double prevCost = costs.get(prevSwing);
                    double totalCost = prevCost + transitionCost + swingCost;

                    // Replace the current cost and bestPrevSwing if it is better
                    if (totalCost < bestCost) {
                        bestCost = totalCost;
                        bestPrevSwing = prevSwing;
                    }
                }

                // Store the best cost and swing calculated
                costs.put(currSwing, bestCost);
                bestPrevSwings.put(currSwing, bestPrevSwing);
            }
        }

        // Get ending swing with the best cost
        T currSwing = proposedSwings
            .getLast()
            .stream()
            .max(Comparator.comparing(costs::get))
            .get();
        // Go backwards and add the optimal swings into output array
        while (bestPrevSwings.containsKey(currSwing)) {
            output.addFirst(currSwing);
            currSwing = bestPrevSwings.get(currSwing);
        }

        return output;
    }

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
    public List<List<T>> predict(BeatmapV3 beatmap, double bpm) {
        // TODO Implement predict

        // Separate colorNotes, arcs, chains by color
        ColorSeparatedPair<List<ColorNote>> colorSeparatedNotes = separateByColor(beatmap.colorNotes);
        ColorSeparatedPair<List<Arc>> colorSeparatedArcs = separateByColor(beatmap.sliders);
        ColorSeparatedPair<List<Chain>> colorSeparatedChains = separateByColor(beatmap.burstSliders);

        // Group turn bombs and bpm into beat pairs
        List<BeatPair<Bomb>> beatPairBombs = toBeatPairs(beatmap.bombNotes);
        List<BeatPair<BpmEvent>> beatPairBpms = toBeatPairs(beatmap.bpmEvents);

        List<List<T>> output = new ArrayList<>();

        // Iterate through each color
        for (int i = 0; i < 2; i++) {
            // Isolate the beatmap objects corresponding to color i
            List<ColorNote> colorNotes = colorSeparatedNotes.get(i);
            List<Arc> arcs = colorSeparatedArcs.get(i);
            List<Chain> chains = colorSeparatedChains.get(i);

            List<List<T>> proposedSwings = propose(beatmap, bpm, beatPairBombs, beatPairBpms, colorNotes, arcs, chains);
            List<T> predictedSwings = predict(proposedSwings);
            output.add(predictedSwings);
        }


        return output;
    }
}

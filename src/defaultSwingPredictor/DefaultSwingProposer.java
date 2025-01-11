package defaultSwingPredictor;

import beatmap.BeatmapV3.*;
import swingPredictor.SwingProposer;
import utils.Vec;
import utils.VecPair;
import utils.Constants;
import utils.MoreMath;
import utils.MoreMath.*;
import utils.DeltaSet;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * Default swing proposer
 */
public class DefaultSwingProposer implements SwingProposer<DefaultSwing> {
    private final double[] DIRTOROT = Constants.DIRTOROT;
    private final Vec[] DIRTOVEC = Constants.DIRTOVEC;
    private final double TWOPI = 2 * Math.PI;
    private final double MINROT = - 3 * Math.PI / 2;
    private final double MAXROT = 5 * Math.PI / 4;
    
    private final double DOTRATIOTHRESHOLD = 0.7;

    /**
     * Gets the angle of a note, taking into account precision rotation
     * @param obj
     * @return
     */
    private double getRot(ColorObject obj) {
        if (obj instanceof ColorNote note) {
            return DIRTOROT[note.d] + note.a * Math.PI / 180.0;
        }
        return DIRTOROT[obj.d];
    }

    /**
     * Gets a unit vector pointing in the direction a given note points at
     * @param obj
     * @return
     */
    private Vec getRotVec(ColorObject obj) {
        if (obj.d == 8) {
            return new Vec(0.0, 0.0);
        }
        return Vec.fromRot(getRot(obj));
    }
    /**
     * Gets the position of a note as a vector
     * @param obj
     * @return
     */
    private Vec getPosVec(PositionedObject obj) {
        return new Vec(obj.x, obj.y);
    }

    /**
     * Gets a pos + rot vector pair representing the end swing given a chain
     * @param chains
     * @return
     */
    private VecPair getSwingEnd(List<Chain> chains) {
        if (chains.isEmpty()) {
            return null;
        }

        // Only take the first Chain
        // TODO: case of multiple chains
        Chain chain = chains.getFirst();

        Vec p0 = getPosVec(chain);
        Vec p2 = new Vec(chain.tx, chain.ty);
        // check if head is pointing directly to tail
        Vec headRotVec = getRotVec(chain);
        Vec diff = p2 .sub (p0);
        diff.normalize();
        double pDist = 0.0;
        // If head is not pointing toward tail, set pDist to half of |diff|
        if (!diff.equals(headRotVec)) {
            pDist = diff.mag() / 2;
        }
        Vec p1 = p0 .add (p2 .mul (pDist));

        Vec resultPos = MoreMath.bezier(p0, p1, p2, chain.s);
        Vec resultRotVec = MoreMath.dBezier(p0, p1, p2, chain.s);

        return new VecPair(resultPos, resultRotVec);
    }

    /**
     * Returns a vague swing given a list of notes and proposed swing rot vector
     * @param notes
     * @param swingRotVec
     * @return
     */
    private VagueSwing snapSwing(List<ColorNote> notes, Vec swingRotVec) {
        VecPair swingEnterInfo = null;
        VecPair swingExitInfo = null;
        // If it is nonzero, use it to determine leaving and entering info (if needed)
        if (!swingRotVec.is0()) {
            // If total cut vector is nonzero
            // determine cut order of all the notes.
            swingRotVec.normalize();

            // First find dot product of note positions and swingRotVec
            Map<ColorNote, Double> noteOrder = new HashMap<>();
            for (ColorNote note: notes) {
                noteOrder.put(note, swingRotVec.dot(getPosVec(note)));
            }

            // Sort by the dot product
            notes.sort((a, b) -> Double.compare(noteOrder.get(a), noteOrder.get(b)));

            // Take the first and last notes to determine the path of the swing
            // TODO: situation where there are multiple notes with the same dot product
            Vec startNotePos = getPosVec(notes.getFirst());
            Vec endNotePos = getPosVec(notes.getLast());
            Vec diff = endNotePos .sub (startNotePos);
            
            if (!diff.is0()) {
                diff.normalize();
                swingRotVec = diff;
            }
            swingEnterInfo = new VecPair(startNotePos, swingRotVec);
            swingExitInfo = new VecPair(endNotePos, swingRotVec);
        }
        return new VagueSwing(swingEnterInfo, swingExitInfo);
    }

    private List<DefaultSwing> noteSwingsFromRot(VecPair enterPair, VecPair exitPair, boolean isForehand, double t0, double t1) {
        double enterRot0 = enterPair.rot;
        double exitRot0 = exitPair.rot;
        Vec enterPos = enterPair.p;
        Vec exitPos = enterPair.p;

        DeltaSet enterRots = new DeltaSet(enterRot0, TWOPI, MINROT, MAXROT);
        DeltaSet exitRots = new DeltaSet(exitRot0, TWOPI);

        List<DefaultSwing> noteSwings = new ArrayList<>();

        // Iterate through every enter rotation
        for (double enterRot: enterRots) {
            double exitRot = exitRots.getClosest(enterRot);
            VecPair noteEnterPair = new VecPair(enterPos, enterRot);
            VecPair noteExitPair = new VecPair(exitPos, exitRot);
            DefaultSwing noteSwing = new DefaultNoteSwing(noteEnterPair, noteExitPair, isForehand, t0, t1);
            noteSwings.add(noteSwing);
        }
        return noteSwings;
    }

    @Override
    public List<DefaultSwing> propose(List<DefaultSwing> prevSwingsProposed,
        List<ColorNote> notes,
        List<Arc> arcHeads,
        List<Arc> arcTails,
        List<Chain> chains,
        List<Bomb> bombs,
        double t0,
        double t1)
    {
        // TODO: Bomb Swings

        // Check if there is anything to hit
        if (!notes.isEmpty() || !chains.isEmpty()) {
            // Find the total cut vector
            Vec totalCutVec = new Vec(0.0, 0.0);
            notes.forEach(note -> totalCutVec.addBy(getRotVec(note)));

            // Try using totalCutVec to determine swing enter and exit info
            VagueSwing proposedSwing = snapSwing(notes, totalCutVec);

            if (!chains.isEmpty()) {
                // If there is a chain, use that to get exit info instead
                proposedSwing.exitInfo = getSwingEnd(chains);
                // Use the chain to calculated enter info if needed
                if (proposedSwing.enterInfo == null) {
                    Chain chain = chains.getFirst();
                    Vec v = getRotVec(chain);
                    proposedSwing.enterInfo = snapSwing(notes, v).enterInfo;
                }
            }

            Set<VagueSwing> proposedSwings = new HashSet<>();

            // If the proposed swing is valid, add it to the list of proposed swings
            if (proposedSwing.enterInfo != null && proposedSwing.exitInfo != null) {
                 proposedSwings.add(proposedSwing);
            }

            // If there is still no enter info and there are multiple notes
            if (proposedSwings.isEmpty() && notes.size() > 1) {
                // Do PCA on the notes, using the difference between the first and last as the initial guess
                Vec p1 = getPosVec(notes.getFirst());
                Vec p2 = getPosVec(notes.getLast());
                Vec guess = p2 .sub (p1);
                List<Vec> notePositions = notes.stream().map(this::getPosVec).toList();
                PCAInfo pcaInfo = MoreMath.pca(notePositions, guess);
                
                // Only add swing if ratio of svs are under threshold
                double ratio = pcaInfo.values[1] / pcaInfo.values[0];
                if (ratio < DOTRATIOTHRESHOLD) {
                    proposedSwing = snapSwing(notes, pcaInfo.pcs[0]);
                    proposedSwings.add(proposedSwing);
                }
            }

            // If there is still no proposed swing, add 8 in all directions
            if (proposedSwings.isEmpty()) {
                for (int i = 0; i < 8; i++) {
                    Vec v = Vec.fromRot(i);
                    proposedSwing = snapSwing(notes, v);
                    proposedSwings.add(proposedSwing);
                }
            }

            // Turn vague swings into note swings
            List<DefaultSwing> noteSwings = new ArrayList<>();
            for (VagueSwing vagueSwing : proposedSwings) {
                // Forehands
                // Get enter and exit rotations
                double enterRot = vagueSwing.enterInfo.v.toRot();
                double exitRot = vagueSwing.exitInfo.v.toRot();
                Vec enterPos = vagueSwing.enterInfo.p;
                Vec exitPos = vagueSwing.exitInfo.p;
                VecPair enterPair = new VecPair(enterPos, enterRot);
                VecPair exitPair = new VecPair(exitPos, exitRot);
                noteSwings.addAll(noteSwingsFromRot(enterPair, exitPair, true, t0, t1));

                // Backhands
                enterRot += Math.PI;
                exitRot += Math.PI;
                enterPair = new VecPair(enterPos, enterRot);
                exitPair = new VecPair(exitPos, exitRot);
                noteSwings.addAll(noteSwingsFromRot(enterPair, exitPair, false, t0, t1));
            }

            return noteSwings;
        }

        return null;
    }
}

package defaultSwingPredictor;

import beatmap.BeatmapV3.*;
import swingPredictor.SwingProposer;
import utils.Vec;
import utils.VecPair;
import utils.Constants;
import utils.Mat;
import utils.LinAlg;
import utils.LinAlg.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Default swing proposer
 */
public class DefaultSwingProposer implements SwingProposer<DefaultSwing> {
    private final double[] DIRTOROT = Constants.DIRTOROT;
    private final Vec[] DIRTOVEC = Constants.DIRTOVEC;
    
    private final double DOTRATIOTHRESHOLD = 0.5;

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
     * Bezier interpolation
     * @param p0
     * @param p1
     * @param p2
     * @param t
     * @return
     */
    private Vec bezier(Vec p0, Vec p1, Vec p2, double t) {
        double a0 = Math.pow((1 - t), 2);
        double a1 = 2 * (1 - t) * t;
        double a2 = t * t;
        return new Vec(
            a0 * p0.x + a1 * p1.x + a2 * p2.x,
            a0 * p0.y + a1 * p1.y + a2 * p2.y
        );
    }

    /**
     * Derivative of bezier
     * @param p0
     * @param p1
     * @param p2
     * @param t
     * @return
     */
    private Vec dBezier(Vec p0, Vec p1, Vec p2, double t) {
        double a0 = 2 * (1 - t);
        double a1 = 2 * t;
        return new Vec(
            a0 * (p0.x - p1.x) + a1 * (p2.x - p1.x),
            a0 * (p0.y - p1.y) + a1 * (p2.y - p1.y)
        );
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

        Vec resultPos = bezier(p0, p1, p2, chain.s);
        Vec resultRotVec = dBezier(p0, p1, p2, chain.s);

        return new VecPair(resultPos, resultRotVec.toRot());
    }

    /**
     * Returns a vague swing given a list of notes and proposed swing rot vector
     * @param notes
     * @param swingRotVec
     * @return
     */
    private VagueSwing getSwing(List<ColorNote> notes, Vec swingRotVec) {
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
        // TODO

        // Check if there is anything to hit
        if (!notes.isEmpty() || !chains.isEmpty()) {
            // Find the total cut vector
            Vec totalCutVec = new Vec(0.0, 0.0);
            notes.forEach(note -> totalCutVec.addBy(getRotVec(note)));

            // Try using totalCutVec to determine swing enter and exit info
            VagueSwing proposedSwing = getSwing(notes, totalCutVec);

            if (!chains.isEmpty()) {
                // If there is a chain, use that to get exit info instead
                proposedSwing.exitInfo = getSwingEnd(chains);
                // Use the chain to calculated enter info if needed
                if (proposedSwing.enterInfo == null) {
                    Chain chain = chains.getFirst();
                    Vec v = getRotVec(chain);
                    proposedSwing.enterInfo = getSwing(notes, v).enterInfo;
                }
            }

            List<VagueSwing> proposedSwings = new ArrayList<>();
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
                PCAInfo pcaInfo = LinAlg.pca(notePositions, guess);
                
                double ratio = pcaInfo.values[1] / pcaInfo.values[0];
                if (ratio < DOTRATIOTHRESHOLD) {
                    proposedSwing = getSwing(notes, pcaInfo.pcs[0]);
                    proposedSwings.add(proposedSwing);
                }
            }

            System.out.println(proposedSwings);
        }

        return null;
    }
}

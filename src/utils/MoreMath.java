package utils;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public abstract class MoreMath {
    private static final double EPS = Constants.EPS;

    public static class PCAInfo {
        public Vec[] pcs = {null, null};
        public double[] values = {0, 0};
        public PCAInfo(Vec pc0, Vec pc1, double sv0, double sv1) {
            pcs[0] = pc1;
            pcs[1] = pc1;
            values[0] = sv0;
            values[1] = sv1;
        }
    }
    public static PCAInfo pca(List<Vec> vecs, Vec guess) {
        // Calculate mean
        Vec mean = new Vec(0.0, 0.0);
        vecs.forEach(v -> mean.addBy(v));
        mean.divBy(vecs.size());

        // Subtract the mean from each Vec
        List<Vec> meanCenteredVecs = vecs.stream()
            .map(v -> v .sub (mean))
            .toList();

        // Get variance of covariance
        double covXY = meanCenteredVecs.stream()
            .mapToDouble(v -> v.x * v.y)
            .sum();
        double varX = meanCenteredVecs.stream()
            .mapToDouble(v -> v.x * v.x)
            .sum();
        double varY = meanCenteredVecs.stream()
            .mapToDouble(v -> v.y * v.y)
            .sum();

        // Construct covariance matrix
        Mat covMat = new Mat(varX, covXY, covXY, varY);

        // Iteratively change guess
        for (int i = 0; i < 5; i++) {
            guess = covMat .mul (guess);
            guess.normalize();
        }

        // Principle components and values
        Vec pc0 = guess;
        Vec pc1 = new Vec(pc0.y, -pc0.x);
        double sv0 = covMat .mul (pc0) .mag();
        double sv1 = covMat .mul (pc1) .mag();

        return new PCAInfo(pc0, pc1, sv0, sv1);
    }

    /**
     * Bezier interpolation
     * @param p0
     * @param p1
     * @param p2
     * @param t
     * @return
     */
    public static Vec bezier(Vec p0, Vec p1, Vec p2, double t) {
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
    public static Vec dBezier(Vec p0, Vec p1, Vec p2, double t) {
        double a0 = 2 * (1 - t);
        double a1 = 2 * t;
        return new Vec(
            a0 * (p0.x - p1.x) + a1 * (p2.x - p1.x),
            a0 * (p0.y - p1.y) + a1 * (p2.y - p1.y)
        );
    }

    /**
     * Affine map
     */
    public static class AffMap implements Function<Double, Double> {
        double m;
        double b;

        /**
         * Constructs an affine map given two source and target points.
         * @param x0 Source 0
         * @param y0 Target 0
         * @param x1 Source 1
         * @param y1 Target 1
         */
        public AffMap(double x0, double y0, double x1, double y1) {
            m = (y1 - y0) / (x1 - x0);
            b = y0 - m * x0;
        }

        /**
         * Constructs an affine map given slope and y-intercept.
         * @param m Slope
         * @param b y-intercept
         */
        public AffMap(double m, double b) {
            this.m = m;
            this.b = b;
        }

        /**
         * Returns the inverse of the current map.
         * @return
         */
        public AffMap getInverse() {
            return new AffMap(1 / m, - b / m);
        }

        @Override
        public Double apply(Double t) {
            return m * t + b;
        }
    }

}

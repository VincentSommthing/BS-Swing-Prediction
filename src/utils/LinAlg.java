package utils;

import java.util.List;

public abstract class LinAlg {
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
}

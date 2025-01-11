package utils;

import utils.MoreMath.*;

import java.util.Iterator;

/**
 * A set of equally spaced numbers with a lower and upper bound
 */
public class DeltaSet implements Iterable<Double> {
    private static double EPS = Constants.EPS;

    double first; // The first element
    double last; // The last element
    double x; // Any element (used as reference point)
    double dx; // Difference between adjacenet elements
    AffMap mapToZ; // Affine map from this to Z
    AffMap mapFromZ; // Affine map from Z to this (inverse of mapToZ)

    /**
     * Construct a delta set without bounds
     * @param x An element that is in the set
     * @param dx The difference between adjacent elements
     */
    public DeltaSet(double x, double dx) {
        this(x, dx, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Construct a delta set with bounds
     * @param x An element in the set, disregarding bounds
     * @param dx The difference between adjacent elements
     * @param lowerBound
     * @param upperBound
     */
    public DeltaSet(double x, double dx, double lowerBound, double upperBound) {
        this.x = x;
        this.dx = Math.abs(dx);
        this.mapToZ = new AffMap(x, 0, x + dx, 1);
        this.mapFromZ = mapToZ.getInverse();
        setBounds(lowerBound, upperBound);
    }

    public void setBounds(double lower, double upper) {
        double mappedLower = mapToZ.apply(lower);
        double mappedUpper = mapToZ.apply(upper);
        double mappedFirst = Math.ceil(mappedLower - EPS);
        double mappedLast = Math.floor(mappedUpper + EPS);

        this.first = mapFromZ.apply(mappedFirst);
        this.last = mapFromZ.apply(mappedLast);
    }

    /**
     * Finds the closest element in the set to y
     * @param y
     * @return
     */
    public double getClosest(double y) {
        double mappedY = mapToZ.apply(y);
        double mappedOutput = Math.round(mappedY);
        double output = mapFromZ.apply(mappedOutput);
        output = Math.clamp(output, first, last);
        return output;
    }

    private class Iter implements Iterator<Double> {
        double current;

        public Iter() {
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current <= last + EPS;
        }

        @Override
        public Double next() {
            return current += dx;
        }
    }

    @Override
    public Iterator<Double> iterator() {
        return new Iter();
    }
}
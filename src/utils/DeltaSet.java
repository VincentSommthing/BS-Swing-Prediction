package utils;

import utils.MoreMath.*;

import java.util.Iterator;

/**
 * A set of equally spaced numbers with a lower and upper bound
 */
public class DeltaSet implements Iterable<Double> {
    private static double EPS = Constants.EPS;

    double first;
    double last;
    double x;
    double dx;

    public DeltaSet(double x, double dx, double lowerBound, double upperBound) {
        this.x = x;
        this.dx = Math.abs(dx);
        setBounds(lowerBound, upperBound);
    }

    public void setBounds(double lower, double upper) {
        // maps this to Z
        AffMap map = new AffMap(x, 0, x + dx, 1);
        double mappedLower = map.apply(lower);
        double mappedUpper = map.apply(upper);
        double mappedFirst = Math.ceil(mappedLower - EPS);
        double mappedLast = Math.floor(mappedUpper + EPS);

        AffMap inverse = map.getInverse();
        this.first = inverse.apply(mappedFirst);
        this.last = inverse.apply(mappedLast);
    }

    private class Iter implements Iterator<Double> {
        double current;

        public Iter() {
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current > last + EPS;
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
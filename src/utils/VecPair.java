package utils;

public class VecPair {
    private double EPS = Constants.EPS;

    public Vec p = null;
    public Vec v = null;
    public double rot = Double.POSITIVE_INFINITY;

    public VecPair(Vec p, Vec v) {
        this.p = p;
        this.v = v;
    }
    public VecPair(Vec p, double rot) {
        this.p = p;
        this.rot = rot;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof VecPair otherVecPair) {
            boolean pEqual = p.equals(otherVecPair.p); 
            boolean vEqual = true;
            boolean rotEqual = true;
            if (v != null) {
                vEqual = v.equals(otherVecPair.v);
            }
            if (!Double.isInfinite(rot)) {
                rotEqual = Math.abs(rot - otherVecPair.rot) < EPS;
            }
            return pEqual && vEqual & rotEqual;
        }
        return false;
    }

    @Override
    public String toString() {
        String posString = "p: (" + this.p.x + ", " + this.p.y + ")  ";
        String vString = "";
        String rotString = "";
        if (v != null) {
            vString = "v: (" + this.v.x + ", " + this.v.y + ")";
        }
        if (!Double.isInfinite(rot)) {
            rotString = "r: " + this.rot;
        }
        return "VecPair(" + posString + vString + rotString + ")";
    }
}

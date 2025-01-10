package utils;
import utils.Constants;

public class Vec {
    private final double EPS = Constants.EPS;

    public double x;
    public double y;

    public Vec (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vec fromRot(double dir) {
        return new Vec(Math.cos(dir - Math.PI / 2), Math.sin(dir - Math.PI / 2));
    }
    public double toRot() {
        return Math.atan2(y, x) + Math.PI / 2;
    }

    public Vec sub(Vec other) {
        return new Vec(x - other.x, y - other.y);
    }

    public Vec add(Vec other) {
        return new Vec(x + other.x, y + other.y);
    }

    public void addBy(Vec other) {
        x += other.x;
        y += other.y;
    }

    public boolean is0() {
        return x * x + y * y < EPS;
    }

    public double dot(Vec other) {
        return x * other.x + y * other.y;
    }

    public double mag() {
        return Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        double l = mag();
        x /= l;
        y /= l;
    }

    public Vec mul(double t) {
        return new Vec(x * t, y * t);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Vec otherVec) {
            return Math.abs(x - otherVec.x) < EPS && Math.abs(y - otherVec.y) < EPS;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Vec(" + String.valueOf(x) + ", " + String.valueOf(y) + ")";
    }
}

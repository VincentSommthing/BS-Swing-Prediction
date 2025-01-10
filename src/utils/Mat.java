package utils;

public class Mat {
    double a;
    double b;
    double c;
    double d;
    public Mat(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Vec mul(Vec v) {
        return new Vec(a * v.x + b * v.y, c * v.x + d *v.y);
    }
}

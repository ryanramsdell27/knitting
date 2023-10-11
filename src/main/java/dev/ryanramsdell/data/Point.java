package dev.ryanramsdell.data;

public class Point {
    public double x;
    public double y;
    public double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void add(Point p) {
        this.x += p.x;
        this.y += p.y;
        this.z += p.z;
    }

    public void scale(double c) {
        this.x *= c;
        this.y *= c;
        this.z *= c;
    }

    @Override
    public String toString() {
        return  String.format("(%f, %f, %f)", x, y, z);
    }
    public String toStringSquare() {
        return  String.format("[%f, %f, %f]", x, y, z);
    }
}

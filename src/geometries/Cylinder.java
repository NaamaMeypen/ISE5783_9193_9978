package geometries;

import primitives.Ray;

public class Cylinder extends Tube{
    final double height;

    public Cylinder(Ray ray, double radius,double height) {
        super(ray, radius);
        this.height=height;
    }

    public double getHeight() {
        return height;
    }
}

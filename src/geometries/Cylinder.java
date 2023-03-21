package geometries;
import primitives.Ray;
import primitives.Vector;
import primitives.point;

import primitives.Ray;

/**
 The Cylinder class represents a cylinder object, which is a type of Tube with a fixed height.
 It inherits the radius and axisRay properties from the Tube class.
 */
public class Cylinder extends Tube{
    final private double height;
    /**
     Creates a new Cylinder object with the given radius, axisRay, and height.
     @param radius the radius of the cylinder
     @param ray the axis ray of the cylinder
     @param height the height of the cylinder
     */
    public Cylinder(Ray ray, double radius,double height) {
        super(ray, radius);
        this.height=height;
    }
    /**
     Returns the height of the cylinder.
     @return the height of the cylinder
     */
    public double getHeight() {
        return height;
    }
    public Vector getNormal(point point) {
        return null;
    }
    @Override
    public String toString(){return super.toString() + ' ' +  height;}
}

package geometries;
import primitives.Ray;
import primitives.Vector;
import primitives.Point;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;

/**
 The Cylinder class represents a cylinder object, which is a type of Tube with a fixed height.
 It inherits the radius and axisRay properties from the Tube class.
 */
public class Cylinder extends Tube {
    final private double height;

    /**
     * Creates a new Cylinder object with the given radius, axisRay, and height.
     *
     * @param radius the radius of the cylinder
     * @param ray    the axis ray of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(Ray ray, double radius, double height) {
        super(ray, radius);
        this.height = height;
    }

    /**
     * Returns the height of the cylinder.
     *
     * @return the height of the cylinder
     */
    public double getHeight() {
        return height;
    }

    public Vector getNormal(Point p0) {
        Vector v = p0.subtract(ray.getP0()); // vector from the center of the cylinder to p0
        double t = ray.getDir().dotProduct(v); // the parameter t where the normal intersects the axis
        if (t == height) { // if p0 is on one of the bases, return the direction vector of the base
            return ray.getDir().normalize();
        }
        if (t == 0) //this is one of the two options
        {
            if (ray.getP0().distance(p0) < radius) //the point p0 is on one of the bases
            {
                return ray.getDir().scale(-1).normalize();
            } else//p0 is orthogonal to the axis ray
                return v;
        }
        return super.getNormal(p0);
    }

    @Override
    public String toString() {
        return super.toString() + ' ' + height;
    }

}

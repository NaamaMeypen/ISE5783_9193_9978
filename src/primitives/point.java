/**

 A class representing a 3D point in space.
 */
package primitives;

import java.awt.*;
import java.util.Collection;
import primitives.Vector.*;

public class point {
    final Double3 xyz;
    /**
     * Constructs a point with the specified x, y, and z coordinates.
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @param z the z-coordinate of the point
     */
    public point( double x,double y, double z)  {
        xyz = new Double3(x,y,z);
    }
    /**
     * Constructs a point with the specified coordinates.
     * @param xyz the coordinates of the point
     */
    point(Double3 xyz) {
        this.xyz = new Double3(xyz.d1, xyz.d2, xyz.d3);
    }

    /**
     * Computes the vector from this point to the specified point.
     * @param p0 the point to compute the vector to
     * @return the vector from this point to the specified point
     */
    public Vector subtract(point p0) {
       return new Vector(xyz.subtract(p0.xyz));
    }
    /**
     * Adds a given vector to the current point and returns a new point object representing the result.
     *
     * @param v0 the vector to add to the current point
     * @return a new point object representing the result of adding the given vector to the current point
     */
   public point add(Vector v0)
    {
        return new point(xyz.add(v0.xyz));
    }
    /**
     * Returns the square of the distance between the current point and the given point.
     *
     * @param p0 the point to which the distance is to be calculated
     * @return the square of the distance between the current point and the given point
     */
    public double distanceSquared(point p0)
    {

        return xyz.subtract(p0.xyz).d1* xyz.subtract(p0.xyz).d1+ xyz.subtract(p0.xyz).d2* xyz.subtract(p0.xyz).d2+xyz.subtract(p0.xyz).d3* xyz.subtract(p0.xyz).d3;
    }

    /**
     * Returns the distance between the current point and the given point.
     *
     * @param p0 the point to which the distance is to be calculated
     * @return the distance between the current point and the given point
     */
    public double distance(point p0){
      return Math.sqrt(distanceSquared(p0));
    }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj instanceof point other)
                return this.xyz.equals(other.xyz);
            return false;
        }
    @Override
    public String toString() { return xyz.toString(); }
     }



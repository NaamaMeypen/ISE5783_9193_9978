package geometries;

import primitives.point;
import primitives.Vector;
/**
 The Geometry interface represents a geometric object that can be intersected with rays

 and has a normal vector at a given point on its surface.
 */
public interface Geometry {
  /**
   Returns the normal vector of the geometry object at the given point.
   @param p0 the point at which to calculate the normal vector
   @return the normal vector of the geometry object at the given point
   */
  public Vector getNormal(point p0);

}

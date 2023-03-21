package geometries;
import primitives.Vector;
import primitives.point;
/**
 The Plane class represents a plane object in three-dimensional space.
 It implements the Geometry interface and provides methods for calculating the plane's normal vector.
 */
public class Plane implements Geometry{

  final private   point q0;
  final private   Vector normal;

    /**
     Creates a new Plane object with the given point and normal vector normalized.
     @param q0 the point on the plane
     @param normal the normal vector of the plane
     */
    public Plane(point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }
    /**
     Creates a new Plane object with three given points.
     This constructor calculates the plane's normal vector using the three points.
     @param p1 the first point on the plane
     @param p2 the second point on the plane
     @param p3 the third point on the plane
     */
    public Plane(point p1, point p2,point p3)
    {
        normal=null;
        q0=p1;
    }

    @Override
    public Vector getNormal(point p0) {
        return normal;
    }
  /**  Returns the normal vector of this object.
    @return the normal vector of this object.
     */
    public Vector getNormal() {
        return normal;
    }
    @Override
    public String toString(){return q0.toString()+ normal.toString();}

    /**
     Returns the point on the plane.
     @return the point on the plane
     */
    public point getPoint() {
        return q0;
    }

}

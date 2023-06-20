package geometries;
import primitives.Ray;
import primitives.Vector;
import primitives.Point;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 The Plane class represents a plane object in three-dimensional space.
 It implements the Geometry interface and provides methods for calculating the plane's normal vector.
 */
public class Plane extends Geometry{

  final private Point q0;
  final private   Vector normal;

    /**
     Creates a new Plane object with the given point and normal vector normalized.
     @param q0 the point on the plane
     @param normal the normal vector of the plane
     */
    public Plane(Point q0, Vector normal) {
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
    public Plane(Point p1, Point p2, Point p3)
    {
        if(p1.equals(p2))
        {
            throw new IllegalArgumentException("It is illegal to Construct a plan with first and second points are " +
                    "converge\n");
        }
        Vector v1=p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        try{
         v1.crossProduct(v2);
        }
        catch (IllegalArgumentException e)
        {

            throw new IllegalArgumentException("It is illegal to construct a plan with three points that are on the same line\n");
        }
        normal=v1.crossProduct(v2).normalize();
        q0=p1;
    }

    @Override
    public Vector getNormal(Point p0) {
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
    public Point getPoint() {
        return q0;
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {

        if (ray.getP0().equals(q0) || isZero(this.normal.dotProduct(ray.getDir()))
                || isZero(this.normal.dotProduct(q0.subtract(ray.getP0()))))
            return null;

        double t = (this.normal.dotProduct(q0.subtract(ray.getP0()))) / (this.normal.dotProduct(ray.getDir()));
        if (t < 0)
            return null;

        //In case there is intersection with the plane return the point
        GeoPoint p = new GeoPoint(this, ray.getPoint(t));
        LinkedList<GeoPoint> result = new LinkedList<GeoPoint>();
        result.add(p);
        return result;
    }
}

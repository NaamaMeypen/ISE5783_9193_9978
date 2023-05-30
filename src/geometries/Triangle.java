package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.isZero;

public class Triangle extends Polygon {
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        //first find the intersections with the plane in which the triangle lays
        List<GeoPoint> intersections = plane.findGeoIntersections(ray);

        //if the plane has no intersections so there are no intersections, so return null
        if (intersections == null)
            return null;//there are no intersection points

        Point p0 = ray.getP0();//the start ray point
        Vector v = ray.getDir();

        Vector v1 = vertices.get(0).subtract(p0);//vector from the ray start point to the polygon vertices
        Vector v2 = vertices.get(1).subtract(p0);//vector from the ray start point to the polygon vertices
        Vector v3 = vertices.get(2).subtract(p0);//vector from the ray start point to the polygon vertices

        double s1 = v.dotProduct(v1.crossProduct(v2)); //s1 = v * (v1 X v2)
        if (isZero(s1))
            return null;//the point is out of triangle

        double s2 = v.dotProduct(v2.crossProduct(v3)); //s2 = v * (v2 X v3)
        if (isZero(s2))
            return null;//the point is out of triangle

        double s3 = v.dotProduct(v3.crossProduct(v1)); //s3 = v * (v3 X v1)
        if (isZero(s3))
            return null;//the point is out of triangle

        //update the geometry
        for (GeoPoint geoPoint : intersections) {
            geoPoint.geometry = this;
        }

        //if they all have the same sign then return the intersections , otherwise return null
        return ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) ? intersections : null;
    }

}
/*
package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

*/
/**
 The Triangle class is a subclass of the Polygon class and represents a triangle in space.
 *//*

public class Triangle extends Polygon {
    */
/**
     * Constructs a new Triangle object with the specified points.
     *
     * @param p0 the first point of the triangle.
     * @param p1 the second point of the triangle.
     * @param p2 the third point of the triangle.
     *//*

    public Triangle(Point p0, Point p1, Point p2) {
        super(p0, p1, p2);
    }

    @Override
    public String toString() {
        return super.toString();
    }

   */
/*private boolean isInside(Ray ray) {
        /*return true if there is an intersection point inside the triangle*//*

      */
/*  List<Point> l = vertices;
        Vector v1 = l.get(0).subtract(ray.getP0());
        Vector v2 = l.get(1).subtract(ray.getP0());
        Vector v3 = l.get(2).subtract(ray.getP0());
        Vector n1 = (v1.crossProduct(v2)).normalize();//new Vector(0.001,0.001,0.001);
        Vector n2 = (v2.crossProduct(v3)).normalize();//new Vector(0.001,0.001,0.001);
        Vector n3 = (v3.crossProduct(v1)).normalize();//new Vector(0.001,0.001,0.001);

        double num1 = n1.dotProduct(ray.getDir());
        double num2 = n2.dotProduct(ray.getDir());
        double num3 = n3.dotProduct(ray.getDir());
        return (num1 > 0 && num2 > 0 && num3 > 0) || (num1 < 0 && num2 < 0 && num3 < 0);
    } *//*


    */
/* public List<GeoPoint> findGeoIntersectionsHelper(Ray ray)
     {
         if(isInside(ray)) // check if there is an intersection point inside the triangle
         {
             //List<GeoPoint> Points =new LinkedList<>();
            // if(plane.findGeoIntersections(ray) != null)
             //Points.add(new GeoPoint(this,plane.findGeoIntersectionsHelper(ray).get(0).point));
             List<GeoPoint> Points=plane.findGeoIntersectionsHelper(ray);
             Points.get(0).geometry=this;
             return  Points;
         }
         return null; // there isn't an intersection point inside the triangle
     }

     *//*

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        // find intersections
        // if the ray inside the plane - return the cross point
        // if the ray not inside the plane (not cross) - return null
        var myList = super.plane.findGeoIntersections(ray);
        if (myList == null)
            return null;

        var P0 = ray.getP0();
        var dir = ray.getDir();

        // the formula
        var v1 = vertices.get(0).subtract(P0);
        var v2 = vertices.get(1).subtract(P0);
        var v3 = vertices.get(2).subtract(P0);

        var n1 = v1.crossProduct(v2).normalize();
        var n2 = v2.crossProduct(v3).normalize();
        var n3 = v3.crossProduct(v1).normalize();

        // check if n1,n2,n3 have the same sign(+\-)
        // -- all of them or bigger the zero or smallest then zero --
        if ((Util.alignZero(n1.dotProduct(dir)) > 0 && Util.alignZero(n2.dotProduct(dir)) > 0
                && Util.alignZero(n3.dotProduct(dir)) > 0) == true
                || (Util.alignZero(n1.dotProduct(dir)) < 0 && Util.alignZero(n2.dotProduct(dir)) < 0
                && Util.alignZero(n3.dotProduct(dir)) < 0) == true)
            return List.of(new GeoPoint(this, myList.get(0).point));
        return null;

    }
}
*/

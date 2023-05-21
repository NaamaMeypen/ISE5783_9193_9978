package primitives;
import geometries.Intersectable.GeoPoint;
import geometries.Intersectable;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a ray in 3D space, defined by a starting point and a direction vector.
 */
public class Ray {
   final private Point p0;
    final private Vector dir;
    /**
     * Constructs a new ray with the given starting point and direction vector.
     * If the length of the direction vector is not 1, it will be normalized.
     *
     * @param p0 the starting point of the ray
     * @param dir the direction vector of the ray
     */
    public Ray(Point p0, Vector dir) {
        this.p0 = p0;
        if(dir.lengthSquared()==1)
        {
            this.dir=dir;
        }
        else {
            this.dir = dir.normalize();
        }
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Ray other)
            return this.p0.equals(other.p0) && this.dir.equals(other.dir);
        return false;
    }
    @Override
    public String toString()
    {
        return "Ray: " +
                "p0="+p0+
            ", dir="+dir.xyz;
    }
// p0.toString()+' '+ dir.toString()
    public Point getP0() {
        return p0;
    }
/** Returns the direction of this Ray*/
    public Vector getDir() {
        return dir;
    }
    public Point getPoint(double t)
    {
        if(isZero(t))
        {
            return p0;
        }
        return p0.add(dir.scale(t));
    }
//    public Point findClosestPoint(List<Point> points){
//        if(points.isEmpty())
//            return null;
//        Point p0= this.getP0();
//        Point shortestDistance= points.get(0);
//        for(Point pt : points){
//            if (p0.distance(pt) < p0.distance(shortestDistance)) {
//                shortestDistance = pt;
//            }
//        }
//
//        return shortestDistance;
//    }
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;
    }
    /**
     * get the closest GeoPoint in the list of points
     * @param points list of intersection points
     * @return the closest point
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> points) {
        if (points == null)
            return null;
        GeoPoint myPoint = points.get(0);
        for (var point : points) {
            if (p0.distance(myPoint.point) > p0.distance(point.point)) {
                myPoint = point;
            }
        }

        return myPoint;
    }
}


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
  /*  @Override
    protected List<Point> findIntersections(Ray ray, double maxDistance) {
        //Step 1 - finding intersection Points with bases:
        GeoPoint gp1 = baseIntersection(this.bottomBase, ray, this.bottomCenter); //intersection Point with bottom base
        GeoPoint gp2 = baseIntersection(this.upperBase, ray, this.upperCenter); //intersection Point with upper base
        if (gp1 != null && gp2 != null) return twoPoints(ray, gp1, gp2);
        GeoPoint basePoint = gp1 != null ? gp1 : gp2;

        //Step 2 - checking if intersection Points with Tube are on Cylinder itself:
        List<GeoPoint> lst = super.findGeoIntersectionsHelper(ray, maxDistance); //intersection Points with Tube
        if (lst == null) return basePoint == null ? null : List.of(basePoint);

        gp1 = checkIntersection(lst.get(0).point);
        gp2 = lst.size() < 2 ? null : checkIntersection(lst.get(1).point);
        if (basePoint != null)
            return gp1 != null ? twoPoints(ray, basePoint, gp1)
                    : gp2 != null ? twoPoints(ray, basePoint, gp2) : List.of(basePoint);
        if (gp1 == null) return gp2 != null ? List.of(gp2) : null;
        return gp2 != null ? twoPoints(ray, gp1, gp2) : List.of(gp1);
    }

    /**
     * If the distance between the intersection point and the center of the sphere is less than the radius of the sphere,
     * then the intersection point is on the sphere
     *
     * @param base   The plane that the cylinder is parallel to.
     * @param ray    The ray that intersects the sphere
     * @param center the center of the sphere
     * @return The intersection point of the ray and the base of the cylinder.
     */
   /* private GeoPoint baseIntersection(Plane base, Ray ray, Point center) {
        List<GeoPoint> lst = base.findGeoIntersections(ray); //intersection Points with Plane
        if (lst == null) return null;
        Point p = lst.get(0).point;
        double radius2 = this.radius * this.radius;
        return alignZero(p.distanceSquared(center) - radius2) < 0 ? new GeoPoint(this, p) : null;
    }

    /**
     * If the point is on the line, return a new GeoPoint object
     *
     * @param p the point to check if it's on the line
     * @return A GeoPoint object.
     */
 /*   private GeoPoint checkIntersection(Point p) {
        if (p == null) return null;
        return alignZero(this.dir.dotProduct(p.subtract(this.bottomCenter))) > 0
                && alignZero(this.dir.dotProduct(p.subtract(this.upperCenter))) < 0
                ? new GeoPoint(this, p) : null;
    }*/


  /*  @Override
    public List<Point> findIntersections(Ray ray)
    {

        Vector dir = ray.getDir();
        Point baseCenter = ray.getP0();
        Point secondBaseCenter = ray.getPoint(height);
        Plane base = new Plane(baseCenter, dir);
        Plane secondBase = new Plane(secondBaseCenter, dir);
        double rdSqr = radius * radius;
        List<Point> temp;

        boolean cond = ray.getDir().equals(dir) || ray.getDir().scale(-1).equals(dir);

        temp = base.findIntersections(ray);
        Point p1 = temp == null || onBase(temp.get(0), baseCenter, rdSqr, cond) ? null : new Point(temp.get(0).getX(),temp.get(0).getY(),temp.get(0).getZ());

        temp = secondBase.findIntersections(ray);
        Point p2 = temp == null || onBase(temp.get(0), secondBaseCenter, rdSqr, cond) ? null : new Point( temp.get(0).getX(),temp.get(0).getY(),temp.get(0).getZ());

        if (p1 != null && p2 != null) {
            if (ray.getP0().distanceSquared(p1) < ray.getP0().distanceSquared(p2)) {
                return List.of(p1, p2);
            } else {
                return List.of(p2, p1);
            }
        }

        Point pBase = p1 != null ? p1 : p2;
        temp = super.findIntersections(ray);

        //No intersections with the casing
        if (temp == null) return pBase == null ? null : List.of(pBase);
        //Two intersections with the casing
        if (temp.size() == 2) {
            //Between the planes
            boolean cond1 = onCylinder(temp.get(0), baseCenter, secondBaseCenter, dir);
            boolean cond2 = onCylinder(temp.get(1), baseCenter, secondBaseCenter, dir);
            if (cond1 && cond2) return temp;
            if (!cond1 && !cond2) return null;
            if (cond1) return List.of(temp.get(0),pBase);
            if (cond2) return List.of(pBase, temp.get(1));
        }

        if (!(onCylinder(temp.get(0), baseCenter, secondBaseCenter, dir))) {//לשים לב שזה ממש בתוך
            return pBase == null ? null : List.of(pBase);
        } else if (pBase == null) {
            return temp;
        }
        double d1 = temp.get(0).distanceSquared(ray.getP0());
        double d2 = pBase.distanceSquared(ray.getP0());

        return d1 < d2 ? List.of(temp.get(0), pBase) : List.of(pBase, temp.get(0));
    }

    private boolean onCylinder(Point p, Point b1, Point b2, Vector dir) {
        return p.subtract(b1).dotProduct(dir) > 0 &&
                p.subtract(b2).dotProduct(dir.scale(-1)) > 0;
    }

    private boolean onBase(Point check, Point center, double rdSqr, boolean cond) {
        return (check.distanceSquared(center) == rdSqr && cond) ||
                (check.distanceSquared(center) > rdSqr);
    }*/
  /*     @Override
    public List<Point> findIntersections(Ray ray) {

        List<Point> res = new ArrayList<>();
        List<Point> lst = super.findIntersections(ray);
        if (lst != null)
            for (Point geoPoint : lst) {
                double distance = alignZero(geoPoint.subtract(ray.getP0()).dotProduct(ray.getDir()));
                if (distance > 0 && distance <= height)
                    res.add(geoPoint);
            }

        if (res.size() == 0)
            return null;
        return res;
    }*/
    /**
     * find intersection points between ray and 3D cylinder
     * @param ray ray towards the sphere
     * @return immutable list containing 0/1/2 intersection points as {@link Point}s objects
     */
//    protected List<Point> findGeoIntersections(Ray ray ) {
//        // origin point of cylinder (on bottom base)
//        Point basePoint=ray.getP0();
//       // Point basePoint=ray.getPoint(-height);
//
//        // point across base point on top base
//        Point topPoint =ray.getPoint(height);
//        // direction vector of cylinder (orthogonal to base point)
//        Vector vC=ray.getDir();
//
//        // find intersection points of ray with bottom base of cylinder
//        List<Point> result= new LinkedList<>();
//        // crate plane that contains base point in it
//        Plane basePlane= new Plane(basePoint,vC);
//        // find intersection between ray and plane
//        List<Point> intersectionsBase=basePlane.findIntersections(ray);
//
//        // if intersections were found, check that point are actually on the base of the cylinder
//        //if distance from base point to intersection point holds the equation ->  distance² < from radius²
//        if(intersectionsBase!=null){
//            Point pt=intersectionsBase.get(0);
//                // intersection point is the base point itself
//                if(pt.equals(basePoint))
//                    result.add(basePoint);
//                    // intersection point is different to base point but is on the bottom base
//                else if(pt.distance(basePoint)<radius)
//                    result.add(pt);
//            }
//
//        // find intersection points of ray with bottom base of cylinder
//        // crate plane that contains top point in it
//        Plane topPlane= new Plane(topPoint,vC);
//        // find intersection between ray and plane
//        List<Point> intersectionsTop=topPlane.findIntersections(ray);
//        // if intersections were found, check that point are actually on the base of the cylinder
//        //if distance from top point to intersection point holds the equation ->  distance² < from radius²
//        if(intersectionsTop!=null) {
//            Point pt = intersectionsTop.get(0);
//            // intersection point is the top point itself
//            if(pt.equals(topPoint))
//                result.add(topPoint);
//
//            // intersection point is different to base point but is on the bottom base
//            else if(pt.distance(topPoint)<radius)
//                result.add(pt);
//
//
//        }
//
//        // if rsy intersects both bases , no other intersections possible - return the result list
//        if (result.size()==2)
//            return result;
//
//        // use tube parent class function to find intersections with the cylinder represented
//        // as an infinite tube
//        List<Point> intersectionsTube=super.findIntersections(ray);
//
//        // if intersection points were found check that they are within the finite cylinder's boundary
//        // by checking if  scalar product fo direction vector with a vector from intersection point
//        // to bottom base point is positive, and scalar product of direction vector with a
//        // vector from intersection point to top base point is negative
//        if(intersectionsTube!=null){
//            for (var pt:intersectionsTube){
//                if(vC.dotProduct(pt.subtract(basePoint))>0 && vC.dotProduct(pt.subtract(topPoint))<0)
//                result.add(pt);
//            }
//        }
//        // return an immutable list
//        int len = result.size();
//        if(len>0)
//            if (len ==1)
//                return List.of(result.get(0));
//            else
//                return List.of(result.get(0), result.get(1));
//
//        // no intersections
//        return null;
//    }
}

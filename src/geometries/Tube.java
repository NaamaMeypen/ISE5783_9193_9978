package geometries;

import primitives.Ray;
import primitives.Util.*;
import primitives.Vector;
import primitives.Point;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 The Tube class is a subclass of RadialGeometry and represents a tube shape in space.
 */
public class Tube extends RadialGeometry {
    /**
     * The axis ray of this tube.
     */
    final protected Ray ray;

    /**
     * Constructs a new Tube object with the specified radius and axis ray.
     *
     * @param radius the radius of this Tube object.
     * @param ray    the axis ray of this Tube object.
     */
    public Tube(Ray ray, double radius) {
        super(radius);
        this.ray = ray;
    }

    /**
     * @return the axis ray of this Tube object.
     */
    public Ray getRay() {
        return ray;
    }

    @Override
    public Vector getNormal(Point p) {
        //p.subtract(ray.getP0()).dotProduct(ray.getDir()))
        double t = (ray.getDir()).dotProduct(p.subtract(ray.getP0()));
        if (isZero(t)) {
            return p.subtract(ray.getP0()).normalize();
            // throw new IllegalArgumentException("it is illegal when  extreme case when p-p0 is orthogonal to v \n");
        }
        Point O = (ray.getP0()).add((ray.getDir()).scale(t));
        return p.subtract(O).normalize();
    }

    @Override
    public String toString() {
        return super.toString() + ray.toString();
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        //The overall idea is to form a quadratic equation that it's
        //solutions are the scale factor for the getPoint method.
        //We form this quadratic equation by setting two restriction on an arbitrary point:
        // 1. It is on the ray (i.e. of the form p+t*v)
        // 2. It is on the tube (i.e. it's distance from the tube axis ray is r)
        //Give those two restrictions we extract the requested quadratic equation.
        Vector tubeDir = this.ray.getDir();
        Vector rayDir = ray.getDir();

        // if the ray is parallel  to the tube axis ray return null
        if (tubeDir.equals(rayDir) || tubeDir.equals(rayDir.scale(-1))) {
            return null;
        }

        double dotP1 = alignZero(rayDir.dotProduct(tubeDir));
        //if rayDir and tubeDir are orthogonal return just the rayDir,
        //else return their dot product.
        Vector vec1 = dotP1 == 0 ? rayDir : rayDir.subtract(tubeDir.scale(dotP1));
        double radiusSquared = this.radius * this.radius;

        //First coefficient of the quadratic equation.
        double a = alignZero(vec1.lengthSquared());

        if (ray.getP0().equals(this.ray.getP0())) {
            return  List.of(new GeoPoint(this, ray.getPoint(Math.sqrt(radiusSquared / a))));
        }

        //The vector between the ray heads.
        Vector deltaP = ray.getP0().subtract(this.ray.getP0());

        //If the ray starts at the tube axis ray
        if (tubeDir.equals(deltaP.normalize()) || tubeDir.equals(deltaP.normalize().scale(-1))) {
            return  List.of(new GeoPoint(this, ray.getPoint(Math.sqrt(radiusSquared / a))));
        }

        double dotP2 = alignZero(deltaP.dotProduct(tubeDir));
        var vec2 = dotP2 == 0 ? deltaP : deltaP.subtract(tubeDir.scale(dotP2));

        //Second coefficient for the quadratic equation
        double b = alignZero(2 * (vec1.dotProduct(vec2)));
        //Third coefficient for the quadratic equation
        double c = alignZero(vec2.lengthSquared() - radiusSquared);

        //Discriminant for the quadratic equation
        double det = alignZero(b * b - 4 * a * c);

        //If the discriminant is smaller or equal to 0,
        // the ray is outside the tube.
        if (det <= 0) return null;

        //Solving the quadratic equation.
        det = Math.sqrt(det);
        double t1 = alignZero((-b + det) / (2 * a));
        double t2 = alignZero((-b - det) / (2 * a));

        if (t1 > 0 && t2 > 0) {
            Point p1 = ray.getPoint(t1);
            double distance1 = ray.getP0().distance(p1);
            Point p2 = ray.getPoint(t2);
            double distance2 = ray.getP0().distance(p2);
            //* if (distance1 <= maxDistance && distance2 <= maxDistance){*//*
            return List.of(new GeoPoint(this,p1),new GeoPoint(this, p2));
        } else if (t1 > 0) {
            Point p1 = ray.getPoint(t1);
            double distance1 = ray.getP0().distance(p1);
            // if (distance1 <= maxDistance)
            return List.of(new GeoPoint(this, p1));
        } else if (t2 > 0) {
            Point p2 = ray.getPoint(t2);
            double distance2 = ray.getP0().distance(p2);
            return List.of(new GeoPoint(this,p2));
        }
        return null;
    }
}



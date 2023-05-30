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
//        double t1 = alignZero((-b + det) / (2 * a));
//        double t2 = alignZero((-b - det) / (2 * a));
//
//        //The intersection points are behind the head of the ray
//        if (t1 <= 0 ) return null;
//        if (t1  > 0) {
//            return t2 <= 0 ? null : List.of(ray.getPoint(t2));
//        } else {
//            //Check if there are one or two intersection points.
//            return t2 <= 0 ? List.of(ray.getPoint(t1)) :
//                    List.of(ray.getPoint(t2), ray.getPoint(t1));
//        }


/*
    @Override
    public List<Point> findIntersections(Ray ray)
    {
        *//*
        The equation for a tube of radius r oriented along a line pa + vat:
        (q - pa - (va,q - pa)va)2 - r2 = 0
        get intersections using formula : (p - pa + vt - (va,p - pa + vt)va)^2 - r^2 = 0
        reduces to at^2 + bt + c = 0
        with a = (v - (v,va)va)^2
             b = 2 * (v - (v,va)va,∆p - (∆p,va)va)
             c = (∆p - (∆p,va)va)^2 - r^2
        where  ∆p = p - pa
        *//*

        Vector v = ray.getDir();
        Vector va = ray.getDir();

        //if vectors are parallel then there is no intersections possible
        if (v.normalize().equals(va.normalize()))
            return null;

        //use of calculated variables to avoid vector ZERO
        double vva;
        double pva;
        double a;
        double b = 0;
        double c = 0;

        //check every variable to avoid ZERO vector
        if (ray.getP0().equals(ray.getP0())){
            vva = v.dotProduct(va);
            if (vva == 0){
                a = v.dotProduct(v);
            }
            else{
                a = (v.subtract(va.scale(vva))).dotProduct(v.subtract(va.scale(vva)));
            }
            b = 0;
            c = - radius * radius;
        }
        else
        {
            Vector deltaP = ray.getP0().subtract(ray.getP0());
            vva = v.dotProduct(va);
            pva = deltaP.dotProduct(va);

            if (vva == 0 && pva == 0){
                a = v.dotProduct(v);
                b = 2 * v.dotProduct(deltaP);
                c = deltaP.dotProduct(deltaP) - radius * radius;
            }
            else if (vva == 0){
                a = v.dotProduct(v);
                Vector scale;
                try {
                    scale = va.scale(deltaP.dotProduct(va));
                    if (deltaP.equals(scale)){
                        b = 0;
                        c = - radius * radius;
                    }
                    else{
                        b = 2 * v.dotProduct(deltaP.subtract(scale));
                        c = (deltaP.subtract(scale).dotProduct(deltaP.subtract(scale))) - radius * radius;
                    }
                } catch (Exception e) {
                    b = 2 * v.dotProduct(deltaP);
                    c = (deltaP).dotProduct(deltaP) - radius * radius;
                }
            }
            else if (pva == 0){
                a = (v.subtract(va.scale(vva))).dotProduct(v.subtract(va.scale(vva)));
                b = 2 * v.subtract(va.scale(vva)).dotProduct(deltaP);
                c = (deltaP.dotProduct(deltaP)) - radius * radius;
            }
            else {
                Vector vSubstractScaleVa;
                Vector scale;
                try {
                    vSubstractScaleVa = v.subtract(va.scale(vva));

                }
                catch (Exception e){
                    vSubstractScaleVa = v;
                }
                a = vSubstractScaleVa.dotProduct(vSubstractScaleVa);
                try {
                    scale = va.scale(deltaP.dotProduct(va));
                    if (deltaP.equals(scale)){
                        b = 0;
                        c = - radius * radius;
                    }
                    else{
                        b = 2 * vSubstractScaleVa.dotProduct(deltaP.subtract(scale));
                        c = (deltaP.subtract(scale).dotProduct(deltaP.subtract(scale))) - radius * radius;
                    }
                } catch (Exception e) {
                    b = 2 * vSubstractScaleVa.dotProduct(deltaP);
                    c = (deltaP.dotProduct(deltaP)) - radius * radius;
                }

            }
        }

        //calculate delta for result of equation
        double delta = b * b - 4 * a * c;

        if (delta <= 0){
            return null; // no intersections
        }
        else{
            //calculate points taking only those with t > 0
            double t1 = alignZero((- b - Math.sqrt(delta)) / (2 * a));
            double t2 = alignZero((- b + Math.sqrt(delta)) / (2 * a));
            if (t1 > 0 && t2 > 0)
            {
                Point p1 = ray.getPoint(t1);
                double distance1 = ray.getP0().distance(p1);
                Point p2 = ray.getPoint(t2);
                double distance2 = ray.getP0().distance(p2);
                *//* if (distance1 <= maxDistance && distance2 <= maxDistance){*//*
                return List.of(p1, p2);
            }
            else if (t1 > 0)
            {
                Point p1 = ray.getPoint(t1);
                double distance1 = ray.getP0().distance(p1);
                // if (distance1 <= maxDistance)
                return List.of(p1);
            }
            else if (t2 > 0)
            {
                Point p2 = ray.getPoint(t2);
                double distance2 = ray.getP0().distance(p2);
                return List.of(p2);
            }
        }
        return null;
    }*/


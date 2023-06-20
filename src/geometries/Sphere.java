package geometries;
import primitives.Ray;
import primitives.Vector;
import primitives.Point;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Sphere extends RadialGeometry{
final private Point center;
    /**
     Constructs a new Sphere object with the specified radius and center point.
     @param radius the radius of this Sphere objec/t.
     @param center the center point of this Sphere object.
     */
    public Sphere(double radius, Point center) {
       super(radius);
       this.center = center;
    }

    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;

    }

    /**
     @return the center point of this Sphere object.
     */
    public Point getCenter() {
        return center;
    }
    @Override
    public Vector getNormal(Point p0) {
//        if(!isZero(p0.distance(center)-radius))
//        {
//            throw new IllegalArgumentException(" The point have to be on the sphere\n");
//        }
        return p0.subtract(center).normalize();
    }
    @Override
    public String toString(){return super.toString()+center.toString();}

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        if (ray.getP0().equals(center))
            return List.of(new GeoPoint(this,ray.getPoint(radius)));
       Vector u= center.subtract(ray.getP0());
       double tm=alignZero (ray.getDir().dotProduct(u));
       double d= alignZero(Math.sqrt(u.lengthSquared()-tm*tm));
       if(d>=radius||isZero(d-radius))
            return null;
        if (d >= radius || isZero(d - radius))
            return null;
        double th =alignZero( Math.sqrt(radius * radius - d * d));
        if (tm - th > 0 && tm + th > 0)
            return List.of(new GeoPoint(this,ray.getPoint(tm - th))
                    ,new GeoPoint(this,ray.getPoint(tm + th)) );
        if (tm - th > 0 && !(tm + th > 0))
            return List.of(new GeoPoint(this,ray.getPoint(tm - th)));
        if (!(tm - th > 0) && tm + th > 0)
            return List.of(new GeoPoint(this, ray.getPoint(tm + th)));
        return null;

    }
}

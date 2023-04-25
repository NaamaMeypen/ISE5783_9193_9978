package geometries;

import primitives.Ray;
import primitives.Vector;
import primitives.Point;

import java.util.List;

import static primitives.Util.isZero;

/**
 The Tube class is a subclass of RadialGeometry and represents a tube shape in space.
 */
public class Tube extends RadialGeometry{
    /**
     The axis ray of this tube.
     */
    final  protected Ray  ray;
    /**
     Constructs a new Tube object with the specified radius and axis ray.
     @param radius the radius of this Tube object.
     @param ray the axis ray of this Tube object.
     */
    public Tube (Ray ray,double radius)
    {
    super(radius);
    this.ray=ray;
     }
    /**
     @return the axis ray of this Tube object.
     */
    public Ray getRay() {
        return ray;
    }
    @Override
    public Vector getNormal(Point p) {
        //p.subtract(ray.getP0()).dotProduct(ray.getDir()))
        double t = (ray.getDir()).dotProduct(p.subtract(ray.getP0()));
        if(isZero(t))
        {
            return p.subtract(ray.getP0()).normalize();
            // throw new IllegalArgumentException("it is illegal when  extreme case when p-p0 is orthogonal to v \n");
        }
        Point O =(ray.getP0()).add((ray.getDir()).scale(t));
        return p.subtract(O).normalize();
    }
    @Override
    public String toString(){
    return super.toString()+ray.toString();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}

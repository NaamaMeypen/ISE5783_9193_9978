package geometries;

import primitives.Ray;
import primitives.Vector;
import primitives.point;
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
    public Vector getNormal(point p0) {
        return null;
    }
    @Override
    public String toString(){
    return super.toString()+ray.toString();
    }
}

package geometries;
import primitives.Vector;
import primitives.point;

public class Sphere extends RadialGeometry{
final private point center;
    /**
     Constructs a new Sphere object with the specified radius and center point.
     @param radius the radius of this Sphere object.
     @param center the center point of this Sphere object.
     */
    public Sphere(double radius,point center) {
       super(radius);
       this.center = center;
    }
    /**
     @return the center point of this Sphere object.
     */
    public point getCenter() {
        return center;
    }
    @Override
    public Vector getNormal(point p0) {
        return null;
    }
    @Override
    public String toString(){return super.toString()+center.toString();}
}

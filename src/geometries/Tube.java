package geometries;

import primitives.Ray;
import primitives.Vector;
import primitives.point;

public class Tube extends RadialGeometry{
final Ray  ray;
public Tube (Ray ray,double radius)
{
    super(radius);
    this.ray=ray;
}
    @Override
    public Vector getNormal(point p0) {
        return null;
    }

    public Ray getRay() {
        return ray;
    }
}

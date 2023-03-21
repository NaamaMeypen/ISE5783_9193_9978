package geometries;

import primitives.Vector;
import primitives.point;

public class Sphere extends RadialGeometry{
final point center;

    public Sphere(double radius,point center) {
       super(radius);
       this.center = center;
    }

    @Override
    public Vector getNormal(point p0) {
        return null;
    }

    public point getCenter() {
        return center;
    }
}

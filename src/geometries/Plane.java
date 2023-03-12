package geometries;

import primitives.Vector;
import primitives.point;

public class Plane implements Geometry{

    point q0;
    Vector normal;

    public Plane(point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }
    public Plane(point p1, point p2,point p3)
    {
        normal=null;
        q0=p1;
    }

    @Override
    public Vector getNormal(point p0) {
        return normal;
    }

    public Vector getNormal() {
        return normal;
    }
}

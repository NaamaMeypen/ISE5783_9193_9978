package primitives;

public class Ray {
    point p0;
    Vector dir;

    public Ray(point p0, Vector dir) {
        this.p0 = p0;
        this.dir = dir.normalize();
    }

}

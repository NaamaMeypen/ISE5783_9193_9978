package primitives;
/**
 * Represents a ray in 3D space, defined by a starting point and a direction vector.
 */
public class Ray {
   final private Point p0;
    final private Vector dir;
    /**
     * Constructs a new ray with the given starting point and direction vector.
     * If the length of the direction vector is not 1, it will be normalized.
     *
     * @param p0 the starting point of the ray
     * @param dir the direction vector of the ray
     */
    public Ray(Point p0, Vector dir) {
        this.p0 = p0;
        if(dir.lengthSquared()==1)
        {
            this.dir=dir;
        }
        else {
            this.dir = dir.normalize();
        }
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Ray other)
            return this.p0.equals(other.p0) && this.dir.equals(other.dir);
        return false;
    }
    @Override
    public String toString()
    {
        return "Ray: " +
                "p0="+p0+
            ", dir="+dir.xyz;
    }
// p0.toString()+' '+ dir.toString()
    public Point getP0() {
        return p0;
    }
/** Returns the direction of this Ray*/
    public Vector getDir() {
        return dir;
    }
    public Point getPoint(double t)
    {
        return p0.add(dir.scale(t));
    }
}


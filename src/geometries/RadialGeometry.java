package geometries;
/**
 The abstract class RadialGeometry is a subclass of Geometry and serves as a blueprint for geometric objects with a radial shape.
 */
public abstract class RadialGeometry extends Geometry
{
    /**
     The radius of this RadialGeometry object.
     */
    protected double radius;
    /**
     Constructs a new RadialGeometry object with the specified radius.
     @param radius the radius of this RadialGeometry object.
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }

    /**
     Returns the radius of this RadialGeometry object.
     @return the radius of this RadialGeometry object.
     */
    public double getRadius() {
        return radius;
    }

}

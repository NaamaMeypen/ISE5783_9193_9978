package geometries;

import primitives.Vector;
import primitives.point;
/**
 The Triangle class is a subclass of the Polygon class and represents a triangle in space.
 */
public class Triangle extends Polygon{
    /**
     Constructs a new Triangle object with the specified points.
     @param p0 the first point of the triangle.
     @param p1 the second point of the triangle.
     @param p2 the third point of the triangle.
     */
    public Triangle(point p0, point p1, point p2) {
        super(p0,p1,p2);
    }
@Override
    public String toString(){return super.toString();}
}

package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

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
    public Triangle(Point p0, Point p1, Point p2) {
        super(p0,p1,p2);
    }
@Override
    public String toString(){return super.toString();}
    private boolean isInside(Ray ray)
    {
        /*return true if there is an intersection point inside the triangle*/
        List<Point> l = vertices;
        Vector v1= l.get(0).subtract(ray.getP0());
        Vector v2= l.get(1).subtract(ray.getP0());
        Vector v3= l.get(2).subtract(ray.getP0());
        Vector n1=(v1.crossProduct(v2)).normalize();//new Vector(0.001,0.001,0.001);
        Vector n2=(v2.crossProduct(v3)).normalize();//new Vector(0.001,0.001,0.001);
        Vector n3=(v3.crossProduct(v1)).normalize();//new Vector(0.001,0.001,0.001);

        double num1 = n1.dotProduct(ray.getDir());
        double num2 = n2.dotProduct(ray.getDir());
        double num3 = n3.dotProduct(ray.getDir());
        return (num1>0&&num2>0&&num3>0)||(num1<0&&num2<0&&num3<0);
    }
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray)
    {
        if(isInside(ray)) // check if there is an intersection point inside the triangle
        {
            //List<GeoPoint> Points =new LinkedList<>();
           // if(plane.findGeoIntersections(ray) != null)
            //Points.add(new GeoPoint(this,plane.findGeoIntersectionsHelper(ray).get(0).point));
            List<GeoPoint> Points=plane.findGeoIntersectionsHelper(ray);
            Points.get(0).geometry=this;
            return  Points;
        }
        return null; // there isn't an intersection point inside the triangle
    }
}

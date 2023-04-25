package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable{
    /**
     * intersectable geometries list
     */
    private List<Intersectable> intersectables;

    /**
     * default Geometries constructor
     */
    public Geometries() {

        intersectables= new LinkedList<>();
    }
    /**
     * Geometries constructor
     * @param  geometries {@link Intersectable}
     */
    public Geometries(Intersectable ... geometries) {
        intersectables= new LinkedList<>();
        Collections.addAll(intersectables,geometries);
    }
    /**
     * add geometries to _intersectables
     * @param geometries {@link Intersectable}
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(intersectables,geometries);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersectionPoints = null;

        //gets list of intersections of all elements with the ray
        for (Intersectable value : intersectables) {
            List<Point> itemPoints = value.findIntersections(ray);
            if (itemPoints != null){
                if(intersectionPoints == null){
                    intersectionPoints = new LinkedList<>();
                }
                for(Point p:itemPoints)
                {
                    intersectionPoints.add(p);

                }
            }
        }
        return intersectionPoints;
    }
}

package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries extends Intersectable{
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
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> intersectionPoints = null;

        //gets list of intersections of all elements with the ray
        for (Intersectable value : intersectables) {
            List<GeoPoint> itemPoints = value.findGeoIntersections(ray);
            if (itemPoints != null){
                if(intersectionPoints == null){
                    intersectionPoints = new LinkedList<>();
                }
                intersectionPoints.addAll(itemPoints);

            }
        }
        return intersectionPoints;
    }
}

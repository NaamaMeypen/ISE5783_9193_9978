package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Geometry;
import java.util.List;

import static primitives.Util.alignZero;

public class RayTracerBasic extends RayTracerBase{
    public RayTracerBasic(Scene scene)
    {
        super(scene);
    }

    /**
     * Get color of the intersection of the ray with the scene
     *
     * @param ray Ray to trace
     * @return Color of intersection
     */
    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections != null) {
            GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);
            return calcColor(closestPoint,ray);
        }
        //ray did not intersect any geometrical object
        return scene.background;
    }

    private Color calcColor(GeoPoint geoPoint,Ray ray)
    {
        return scene.ambientLight.getIntensity()
                .add(geoPoint.geometry.getEmission())
                .add(calcLocalEffects(geoPoint,ray));
    }
    private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
        Color col= intersection.geometry.getEmission();
        Vector v = ray.getDir();
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv= alignZero(n.dotProduct(v));
        if (nv== 0) return col;
        Material material=intersection.geometry.getMaterial();
        //int nShininess= intersection.geometry.getMaterial().nShininess;
        //double kd= intersection.geometry.getMaterial().kD;
        //double ks = intersection.geometry.getMaterial().kS;
        for (LightSource lightSource: scene.lights)
        {
            Vector l = lightSource.getL(intersection.point);
            double nl= alignZero(n.dotProduct(l));
            if (nl * nv> 0)
            { // sign(nl) == sing(nv)
                Color lightIntensity= lightSource.getIntensity(intersection.point);
                col = lightIntensity.scale(calcDiffusive(material, nl)+calcSpecular(material, n, l,nl, v)).add(col);
            }
        }
        return col;
    }

    private double calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {

        Vector r = l.subtract(n.scale(2 * nl));
        double minusVR= alignZero(-v.dotProduct(r));
        if (minusVR <= 0)
            return 0;
        //color = ks * max(0, -v.r)^nSh @ppt 7 theoretical course
        return material.kS* (Math.pow(Math.max(0, minusVR), material.nShininess));
    }

    /**
     * Calculate color of the diffusive effects of the light
     *
     * @param kd diffusive ratio
     * @param l light's direction vector
     * @param n normal vector
     * @param lightIntensity intensity of the light
     * @return color of the diffusive effect
     */
    private double calcDiffusive(Material material, double nl) {
        return material.kD*(nl > 0 ? nl : -nl);

    }

}

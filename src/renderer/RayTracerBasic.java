package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static java.lang.Math.*;
import static primitives.Util.alignZero;

public class RayTracerBasic extends RayTracerBase{
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
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
                .add(calcLocalEffect(geoPoint,ray));
    }
    /**
     * help to calculate "calcColor" - calculated light contribution from all light
     * sources
     *
     * @param intersection - point on geometry body
     * @param ray          - ray from the camera
     * @param kkt            - the current attenuation level
     * @return calculated light contribution from all light sources
     */
    /*private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
        Vector v = ray.getDir();
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = Util.alignZero(n.dotProduct(v));
        if (nv == 0)
            return Color.BLACK;
        var material = intersection.geometry.getMaterial();
        int nShininess = material.nShininess;
        double kd = material.kD;
        double ks = material.kS;
        Color color = Color.BLACK;
        for (LightSource lightSource : scene.lights)
        {
            Vector l = lightSource.getL(intersection.point);
            double nl = Util.alignZero(n.dotProduct(l));
            if (nl * nv > 0)
            { // sign(nl) == sign(nv)
                Color lightIntensity = lightSource.getIntensity(intersection.point);
                color = color.add(calcDiffusive(kd, nl, lightIntensity),
                        calcSpecular(ks, n, l, nl, v, nShininess, lightIntensity));

            }
        }
        return color;
    }*/
    private Color calcLocalEffect(GeoPoint intersection, Ray ray) {
        Vector v = ray.getDir();
        Vector n = intersection.geometry.getNormal(intersection.point);

        double nv = alignZero(n.dotProduct(v)); //nv=n*v
        if (nv == 0) {
            return Color.BLACK;
        }

        int nShininess = intersection.geometry.getMaterial().nShininess;
        Double3 kd = intersection.geometry.getMaterial().kD;
        Double3 ks = intersection.geometry.getMaterial().kS;
        Color color = Color.BLACK; //base color

        //for each light source in the scene
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(intersection.point); //the direction from the light source to the point
            double nl = alignZero(n.dotProduct(l)); //nl=n*l

            //if sign(nl) == sign(nv) (if the light hits the point add it, otherwise don't add this light)
            if (nl * nv > 0) {
                if(unshaded(intersection,l,n,nl,lightSource)) {
                    Color lightIntensity = lightSource.getIntensity(intersection.point);
                    color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                            calcSpecular(ks, l, n, v, nShininess, lightIntensity));
                }
            }
        }
        return color;
    }
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, double nl,LightSource ls) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Vector epsVector = n.scale(nl < 0 ? DELTA : -DELTA);
        Point point = gp.point.add(epsVector);
        Ray lightRay = new Ray(point, lightDirection);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
        if (intersections != null) {
            for (GeoPoint gp2 : intersections) {
                if ( point.distance(gp2.point)<ls.getDistance(point)) {
                    return false;
                }
            }
        }
        return true;
    }
    // The above code is checking if the point is unshaded. If it is unshaded, it will return true. If it is shaded, it
    // will return false.
//    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource ls){
//        Vector lightDir = l.scale(-1);// from point to light source
//        Vector epsVector= n.scale(n.dotProduct(lightDir)>0?DELTA:-DELTA);
//        Point point = gp.point.add(epsVector);
//        Ray lightRay = new Ray(point, n, lightDir);
//        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
//        if (intersections != null) {
//            for (GeoPoint gp2 : intersections) {
//                if ( point.distance(gp2.point)<ls.getDistance(point) && gp2.geometry.getMaterial().kT.equals(new Double3(0.0))) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//    private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
//        Color col= intersection.geometry.getEmission();
//        Vector v = ray.getDir();
//        Vector n = intersection.geometry.getNormal(intersection.point);
//        double nv= alignZero(n.dotProduct(v));
//        if (nv== 0) return col;
//        Material material=intersection.geometry.getMaterial();
//        //int nShininess= intersection.geometry.getMaterial().nShininess;
//        //double kd= intersection.geometry.getMaterial().kD;
//        //double ks = intersection.geometry.getMaterial().kS;
//        for (LightSource lightSource: scene.lights)
//        {
//            Vector l = lightSource.getL(intersection.point);
//            double nl= alignZero(n.dotProduct(l));
//            if (nl * nv> 0)
//            { // sign(nl) == sing(nv)
//                Color lightIntensity= lightSource.getIntensity(intersection.point);
//                col = lightIntensity.scale(calcDiffusive(material, nl)+calcSpecular(material, n, l,nl, v)).add(col);
//            }
//        }
//        return col;
//    }
    /**
     * calculate the specular light according to Phong's model
     * @param ks - Coefficient for specular
     * @param l - vector from light source
     * @param n - normal to the point
     * @param v - camera vector
     * @param nShininess - exponent
     * @param lightIntensity - Light intensity
     * @return the specular light
     */
    /*private Color calcSpecular(double ks, Vector l, Vector n, double nl, Vector v, int nShininess, Color lightIntensity)
        {
        Vector r = l.add(n.scale(-2*n.dotProduct(l))).normalize();
        double result = Util.alignZero(-v.dotProduct(r));
        if (result <= 0)
            return Color.BLACK;
        return lightIntensity.scale(ks*(Math.pow(Math.max(0, result), nShininess)));

    }
*/
    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        double ln = alignZero(l.dotProduct(n)); //ln=l*n
        Vector r = l.subtract(n.scale(2 * ln)).normalize(); //r=l-2*(l*n)*n
        double vr = alignZero(v.dotProduct(r)); //vr=v*r
        double vrnsh = pow(max(0, -vr), nShininess); //vrnsh=max(0,-vr)^nshininess
        return lightIntensity.scale(ks.scale(vrnsh)); //Ks * (max(0, - v * r) ^ Nsh) * Il
    }
//    private double calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {
//
//        Vector r = l.subtract(n.scale(2 * nl));
//        double minusVR= alignZero(-v.dotProduct(r));
//        if (minusVR <= 0)
//            return 0;
//        //color = ks * max(0, -v.r)^nSh @ppt 7 theoretical course
//        return material.kS* (Math.pow(Math.max(0, minusVR), material.nShininess));
//    }

//    /**
//     * Calculate color of the diffusive effects of the light
//     *
//     * @param kD diffusive ratio
//     * @param l light's direction vector
//     * @param n normal vector
//     * @param lightIntensity intensity of the light
//     * @return color of the diffusive effect
//     */
//    private double calcDiffusive(Material material, double nl) {
//        return material.kD*(nl > 0 ? nl : -nl);
//
//    }
    /**
     * calculate the diffusive light according to Phong's model
     * @param kd - Coefficient for diffusive
     * @param l - vector from light source
     * @param nl - normal to the point
     * @param lightIntensity - Light intensity
     * @return the diffusive light
     */
  /*  private Color calcDiffusive(double kd, double nl, Color lightIntensity) {
        return lightIntensity.scale((kd*(nl >= 0 ? nl : -nl)));
    }*/
    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color lightIntensity) {
        double ln = alignZero(abs(l.dotProduct(n))); //ln=|l*n|
        return lightIntensity.scale(kd.scale(ln)); //Kd * |l * n| * Il
    }

    /**
     * Construct the ray refracted by an intersection with the geometry
     * @param n normal to the geometry at intersection
     * @param point the intersection point
     * @param innerRay the ray entering
     * @return refracted ray (in our project there is no refraction incidence, so we return a new ray with the same characteristics)
     */
    private Ray constructRefractedRay(Vector n, Point point, Ray innerRay) {
        return new Ray(point,n,innerRay.getDir());
    }
    /**
     * Construct the ray getting reflected on an intersection point
     * @param n normal to the point
     * @param point the intersection point
     * @param innerRay the ray entering at the intersection
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Vector n, Point point, Ray innerRay) {
        //r = v - 2 * (v*n) * n
        //r is the reflected ray
        Vector v = innerRay.getDir();
        Vector r = null;
        try {
            r = v.subtract(n.scale(v.dotProduct(n)).scale(2)).normalize();
        } catch (Exception e) {
            return null;
        }
        return new Ray(point, n,r);
    }
    /**
     * Find the closest intersection point between a ray base and the scene's geometries
     * @param ray the ray
     * @return the closest point
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> geoPoints = scene.geometries.findGeoIntersections(ray);
        return ray.findClosestGeoPoint(geoPoints);
    }

}

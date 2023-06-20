package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static java.lang.Math.*;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class RayTracerBasic extends RayTracerBase{
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double INITIAL_K = 1.0;

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
        if (this.findClosestIntersection(ray) != null)
            return calcColor(this.findClosestIntersection(ray), ray);
        return scene.background;

    }

    /**
     * help to calculate "calcColor" - calculated light contribution from all light
     * sources
     *
     * @param intersection - point on geometry body
     * @param ray          - ray from the camera
     * @param k           - the current attenuation level
     * @return calculated light contribution from all light sources
     */
    private Color calcColor(GeoPoint intersection, Ray ray,int level,Double3 k) {
        Color color = calcLocalEffect(intersection,ray.getDir(),k);
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, ray.getDir(), level, k));

    }
    /**
     * Calculate color using recursive function
     *
     * @param geopoint the point of intersection
     * @param ray      the ray
     * @return the color
     */
    private Color calcColor(GeoPoint geopoint, Ray ray) {
        return calcColor(geopoint, ray, MAX_CALC_COLOR_LEVEL,new Double3(INITIAL_K))
                .add(scene.ambientLight.getIntensity());

    }

    private Color calcGlobalEffect(Ray ray, int level, Double3 kx, Double3 kkx) {
        GeoPoint gp = findClosestIntersection(ray);

        if (gp == null) {
            return scene.background;
        }

        return calcColor(gp, ray, level - 1, kkx).scale(kx);
    }
    private Color calcGlobalEffects(GeoPoint intersection, Vector inRay, int level, Double3 k) {
        Color color = Color.BLACK; //base color
        Vector n = intersection.geometry.getNormal(intersection.point); //normal

        //reflection attenuation of the material
        Double3 kr = intersection.geometry.getMaterial().kR;
        //reflection level as affected by k
        Double3 kkr = kr.product(k);

        if (!kkr.lowerThan(MIN_CALC_COLOR_K)) { //if the reflection level is not lower than the minimum
            //construct a reflection  ray from the point
            Ray reflectedRay = constructReflectedRay(n, intersection.point, inRay);

            //add this color to the point by recursively calling calcGlobalEffect
            color = calcGlobalEffect(reflectedRay, level, kr, kkr);
        }


        //transparency  attenuation factor of the material
        Double3 kt = intersection.geometry.getMaterial().kT;
        //transparency level
        Double3 kkt = kt.product(k);

        if (!kkt.lowerThan(MIN_CALC_COLOR_K)) {//if the transparency level is not lower than the minimum
            //construct a refracted ray from the point
            Ray refractedRay = constructRefractedRay(n, intersection.point, inRay);

            //add to the color to the point by recursively calling calcGlobalEffect
            color = color.add(calcGlobalEffect(refractedRay, level, kt, kkt));
        }

        return color;
    }


        /*** Calculate the local effect of light sources on a point
     ** @param intersection the point
     * @param v            the direction of the ray from the viewer
     * @param k            the kR or kT factor at this point
     * @return the color
     */
    private Color calcLocalEffect(GeoPoint intersection, Vector v, Double3 k) {
        Vector n = intersection.geometry.getNormal(intersection.point);

        double nv = alignZero(n.dotProduct(v)); //nv=n*v
        if (isZero(nv)) {
            return Color.BLACK;
        }

        int nShininess = intersection.geometry.getMaterial().nShininess;
        Double3 kd = intersection.geometry.getMaterial().kD;
        Double3 ks = intersection.geometry.getMaterial().kS;

        Color color = intersection.geometry.getEmission(); //base color

        //for each light source in the scene
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(intersection.point); //the direction from the light source to the point
            double nl = alignZero(n.dotProduct(l)); //nl=n*l
            //if sign(nl) == sign(nv) (if the light hits the point add it, otherwise don't add this light)
            if (nl * nv > 0) {
                //ktr is the level of shade on the point (according to transparency of material)
                Double3 ktr = transparency(intersection, l, n,lightSource );
                if (!(ktr.product(k)).lowerThan(MIN_CALC_COLOR_K)) {
                    Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
                    color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                            calcSpecular(ks, l, n, v, nShininess, lightIntensity));
                }
            }
        }
        return color;
    }
        /**
     * Returns wether a certain point is shaded by other objects
     *
     * @param gp          the point
     * @param l           the direction of the light
     * @param n           normal to the point
     * @param lightSource the loght source
     * @return true if the point is shaded
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource lightSource, double nv) {
        Vector lightDirection = l.scale(-1); //vector from the point to the light source

        Vector deltaVector = n.scale(nv < 0 ? DELTA : -DELTA);
        Point p = gp.point.add(deltaVector);
        Ray lightRay = new Ray(p, lightDirection);

        double lightDistance = lightSource.getDistance(gp.point);
        //finding only points that are closer to the point than the light
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);

        //if there are no intersections return true (there is no shadow)
        if (intersections == null) {
            return true;
        }

        //for each intersection
        for (GeoPoint intersection : intersections) {
            //if the material is not transparent return false
            if (intersection.geometry.getMaterial().kT == Double3.ZERO) {
                return false;
            }

        }
        return true;
    }


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
    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        double ln = alignZero(l.dotProduct(n)); //ln=l*n
        Vector r = l.subtract(n.scale(2 * ln)).normalize(); //r=l-2*(l*n)*n
        double vr = alignZero(v.dotProduct(r)); //vr=v*r
        double vrnsh = pow(max(0, -vr), nShininess); //vrnsh=max(0,-vr)^nshininess
        return lightIntensity.scale(ks.scale(vrnsh)); //Ks * (max(0, - v * r) ^ Nsh) * Il
    }

    /**
     * calculate the diffusive light according to Phong's model
     * @param kd - Coefficient for diffusive
     * @param l - vector from light source
     * @param lightIntensity - Light intensity
     * @return the diffusive light
     */

    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color lightIntensity) {
        double ln = alignZero(abs(l.dotProduct(n))); //ln=|l*n|
        return lightIntensity.scale(kd.scale(ln)); //Kd * |l * n| * Il
    }

    /**
     * Construct the ray refracted by an intersection with the geometry
     * @param n normal to the geometry at intersection
     * @param point the intersection point
     * @param innerVec the ray entering
     * @return refracted ray (in our project there is no refraction incidence, so we return a new ray with the same characteristics)
     */
    private Ray constructRefractedRay(Vector n, Point point, Vector innerVec) {
        return new Ray(point,n,innerVec);
    }
    /**
     * Construct the ray getting reflected on an intersection point
     * @param n normal to the point
     * @param point the intersection point
     * @param innerVec the ray entering at the intersection
     * @return the reflected ray
     */
    private Ray constructReflectedRay(Vector n, Point point, Vector innerVec) {
        //r = v - 2 * (v*n) * n
        //r is the reflected ray
        Vector r = null;
        try {
            r = innerVec.subtract(n.scale(innerVec.dotProduct(n)).scale(2)).normalize();
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
    /**
     * The function returns the transparency of the point, which is the product of the transparency of the point's geometry
     * and the transparency of all the geometries between the point and the light source
     *
     * @param gp The point on the geometry that we're currently shading.
     * @param l the vector from the point to the light source
     * @param n the normal vector of the point
     * @param ls the light source
     * @return The transparency of the point.
     */
    private Double3 transparency(GeoPoint gp, Vector l, Vector n, LightSource ls) {
        Vector lightDir = l.scale(-1);// from point to light source
        Vector epsVector = n.scale(n.dotProduct(lightDir) > 0 ? DELTA : -DELTA);
        Point point = gp.point.add(epsVector);
        Ray lightRay = new Ray(point, n, lightDir);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);
        Double3 ktr = new Double3(1);
        if (intersections != null) {
            for (GeoPoint gp2 : intersections) {
                if (point.distance(gp2.point) < ls.getDistance(point)) {
                    ktr=gp2.geometry.getMaterial().kT.product(ktr);
                }
            }
        }
        return ktr;
    }

}

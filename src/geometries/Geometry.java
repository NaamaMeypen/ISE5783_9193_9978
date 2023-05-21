package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Vector;
import primitives.Point;

/**
 The Geometry interface represents a geometric object that can be intersected with rays

 and has a normal vector at a given point on its surface.
 */
 public abstract class Geometry extends Intersectable
{
  /**
   * Returns the normal vector of the geometry object at the given point.
   *
   * @param p0 the point at which to calculate the normal vector
   * @return the normal vector of the geometry object at the given point
   */
  protected Color emission = Color.BLACK;

    public Material getMaterial() {
        return material;
    }

    private Material material=new Material();

    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public abstract Vector getNormal(Point p0);

 public Color getEmission()
 {
  return emission;
 }

 public Geometry setEmission(Color emission) {
  this.emission = emission;
  return this;
 }
}

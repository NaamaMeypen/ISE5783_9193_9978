package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;
/**
 * abstract class for ray tracer
 */
public abstract class RayTracerBase {
    protected Scene scene;
    /**
     * scene ctor
     * @param scene {@link Scene}
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * @param ray{@link Ray)
     * @return Color
     */
    public abstract Color traceRay(Ray ray);
}

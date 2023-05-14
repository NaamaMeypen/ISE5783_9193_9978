package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight {
    public static AmbientLight NONE=new AmbientLight(Color.BLACK,Double3.ZERO);
    private final Color intensity; // intensity of ambient light
    /**
     * Ctor
     * @param Ia light illumination
     * @param Ka light factor
     */
    public AmbientLight(Color Ia , Double3 Ka)
    {
        intensity = Ia.scale(Ka);
    }
    /**
     * Ctor
     * @param Ia light illumination
     * @param Ka light factor
     */
    public AmbientLight(Color Ia, double Ka )
    {
        intensity=Ia.scale(Ka);
    }

    /**
     * getter for intensity
     * @return intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}

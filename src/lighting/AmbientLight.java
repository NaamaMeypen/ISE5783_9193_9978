package lighting;

import primitives.Color;
import primitives.Double3;

public class AmbientLight extends Light{
    public static AmbientLight NONE=new AmbientLight(Color.BLACK,Double3.ZERO);
    /**
     * Ctor
     * @param Ia light illumination
     * @param Ka light factor
     */
    public AmbientLight(Color Ia , Double3 Ka)
    {
        super(Ia.scale(Ka));
    }
    /**
     * Ctor
     * @param Ia light illumination
     * @param Ka light factor
     */
    public AmbientLight(Color Ia, double Ka )
    {

        super(Ia.scale(Ka));
    }

    public AmbientLight(Color intensity) {
        super(intensity);
    }
}

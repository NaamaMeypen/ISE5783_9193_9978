package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class DirectionalLight extends Light implements LightSource{
    private Vector direction;
    protected DirectionalLight(Color intensity) {
        super(intensity);
    }

    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction;
    }

    @Override
    public Color getIntensity(Point p)
    {
        //intensity of directional light is the same in every point
        return super.getIntensity();
    }

    @Override
    public Vector getL(Point p)
    {
        return direction.normalize();
    }
}

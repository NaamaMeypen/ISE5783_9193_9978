package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static java.lang.Math.pow;
import static primitives.Util.isZero;

public class SpotLight extends PointLight{
    private Vector direction;
    public double narrowBeam =1;


    public SpotLight(Color intensity, Point position, Vector direction,double narrowBeam)throws IllegalArgumentException {
        super(intensity, position);
        this.direction = direction.normalize();
        if(narrowBeam>1 || narrowBeam<0)
            throw new IllegalArgumentException("Factor has to be between 0-1");
        this.narrowBeam =narrowBeam;
    }
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    protected SpotLight(Color intensity) {
        super(intensity);
    }
    @Override
    public Vector getL(Point p)
    {
        return super.getL(p);
    }
    @Override
    public Color getIntensity(Point p) {
        double proj = direction.dotProduct(getL(p)); //direction*(psition-p) , projection of light on point
        //if the light source doesn't hit the point return color black
        if (isZero(proj))
            return Color.BLACK;

        double factor = Math.max(0, proj);
        Color i0 = super.getIntensity(p);
        factor=pow(factor, narrowBeam);


        return i0.scale(factor);
    }

    public SpotLight setNarrowBeam(int narrowBeam) {
        this.narrowBeam =narrowBeam;
        return this;
    }
}

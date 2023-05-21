package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource{
    private Point position;
    private double kC=1;
    private double kL=0;
    private double kQ=0;

    public PointLight setkC(double kC) {
        this.kC = kC;
        return this;
    }

    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    public PointLight setkL(double kL) {
        this.kL = kL;
        return this;
    }

    public PointLight setkQ(double kQ) {
        this.kQ = kQ;
        return this;
    }

    protected PointLight(Color intensity) {
        super(intensity);
    }

    @Override
    public Color getIntensity(Point p) {
        double d = p.distance(this.position);
        return this.getIntensity().reduce(kC+(kL*d)+(kQ*d * d));
    }

    @Override
    public Vector getL(Point p) {
        try {
            return p.subtract(position).normalize();
        } catch (Exception exception) {
            return null;
        }
    }
}

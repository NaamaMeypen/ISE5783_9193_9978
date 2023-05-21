package primitives;

public class Material {
    public double kD = 0 ;
    public double kS=0;
    public int nShininess=0;

    public Material setkD(double kD) {
        this.kD = kD;
        return this;
    }
    public Material setkD(Double3 kD) {
        kD = kD;
        return this;
    }

    public Material setkS(double kS) {
        this.kS = kS;
        return this;
    }
    public Material setkS(Double3 kS) {
        kS = kS;
        return this;
    }
    public Material setnShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}

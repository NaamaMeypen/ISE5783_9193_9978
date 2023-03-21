package primitives;

import java.awt.*;
import java.util.Collection;
import primitives.Vector.*;

public class point {
    final Double3 xyz;
    public point( double x,double y, double z)  {
        xyz = new Double3(x,y,z);
    }
    point(Double3 xyz) {
        this.xyz = new Double3(xyz.d1, xyz.d2, xyz.d3);
    }

    public Vector subtract(point p0) {
       return new Vector(xyz.subtract(p0.xyz));
    }

   public point add(Vector v0)
    {
        return new point(xyz.add(v0.xyz));
    }
    public double distanceSquared(point p0)
    {

        return xyz.subtract(p0.xyz).d1* xyz.subtract(p0.xyz).d1+ xyz.subtract(p0.xyz).d2* xyz.subtract(p0.xyz).d2+xyz.subtract(p0.xyz).d3* xyz.subtract(p0.xyz).d3;
    }
    public double distance(point p0){
      return Math.sqrt(distanceSquared(p0));
    }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj instanceof point other)
                return this.xyz.equals(other.xyz);
            return false;
        }
    @Override
    public String toString() { return "Point:" + xyz; }
     }



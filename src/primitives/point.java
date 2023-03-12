package primitives;

import java.awt.*;
import java.util.Collection;
import primitives.Vector.*;

public class point {
    final Double3 xyz;
    public point(int x, int y, int z) {
        xyz = new Double3(x,y,z);
    }
    point(Double3 xyz) {
        this.xyz = xyz;
    }
    //  return new Vector((p0.xyz).subtract(xyz));

    public Vector subtract(point p0) {
       return new Vector(xyz.subtract(p0.xyz));
    }

   public point add(Vector v0)
    {
        return  new point((((point)v0).xyz).add(xyz));
    }
    public double distanceSquared(point p0)
    {
        Double3 d0=xyz.subtract(p0.xyz);
        return d0.d1* d0.d1+ d0.d2* d0.d2+d0.d3* d0.d3;
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
}

package primitives;

import java.awt.*;
import java.util.Collection;
import java.util.Vector;

public class point {
    final Double3 xyz;
    public point(int x, int y, int z) {
        xyz = new Double3(x,y,z);
    }
    point(Double3 xyz) {
        this.xyz = xyz;
    }

    public Vector subtract(point p0) {
        return new Vector(this.xyz.subtract(p0.xyz));
 //  return new Vector(p0.xyz.subtract(this.xyz));
    }
   public point add(Vector v0)
    {
        Point p0=v0(point);
        return  p0.xyz.add(xyz);
      //  return new point(d0);
     //   return this.xyz.add(((Point)v0).xyz);
    }
}

 package primitives;
import java.awt.*;
import java.util.Collection;

public class Vector extends point {

     public Vector(int x, int y, int z) {
        super(x, y, z);
        if(super.xyz.equals(Double3.ZERO))
        {
            throw new IllegalArgumentException("Vector zero is illegal\n");
        }
    }

     Vector(Double3 xyz)
     {
         super(xyz);
         if(xyz.equals(Double3.ZERO))

         {
             throw new IllegalArgumentException("Vector zero is illegal\n");

         }
    }

   public Vector add(Vector v0) {
       return new Vector(super.xyz.add(((point)v0).xyz));
    }
    public Vector scale(double rhs){
       return new Vector(super.xyz.scale(rhs));

    }
    public double dotProduct(Vector v0)
    {
        return super.xyz.d1*((point)v0).xyz.d1 +super.xyz.d2*((point)v0).xyz.d2 + super.xyz.d3*((point)v0).xyz.d3;
    }
    public Vector crossProduct(Vector v0){
        double u1 = xyz.d1;
        double u2 =  xyz.d2;
        double u3 =  xyz.d3;
        double v1 = v0. xyz.d1;
        double v2 = v0. xyz.d2;
        double v3 = v0. xyz.d3;
      return new Vector(new Double3( u2 * v3 - u3 * v2, u3 * v1 - u1 * v3, u1 * v2 - u2 * v1));
    }
    public double lengthSquared()
    {
        return dotProduct(this);
    }
public double length()
{
    return Math.sqrt(lengthSquared());
}
public Vector normalize()
{
    double len= this.length();
    return new Vector(super.xyz.reduce(len));
}
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Vector other)
            return super.equals((point)other);
        return false;
    }
}

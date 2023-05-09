package primitives;

/**
 * The Vector class represents a mathematical vector in three-dimensional space.
 * It extends the Point class and inherits its x, y, and z coordinates.
 * A Vector can be created using its x, y, and z components or by passing a Double3 object.
 * A Vector cannot be created with all components equal to zero.
 * This class provides methods for vector addition, scaling, dot product, cross product,
 * and normalization.
 */

public class Vector extends Point {

    /**
     * Constructs a Vector with the specified x, y, and z components.
     *
     * @param x the x component of the vector
     * @param y the y component of the vector
     * @param z the z component of the vector
     * @throws IllegalArgumentException if all components are equal to zero
     */

    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector zero is illegal\n");
        }
    }

    /**
     * Constructs a Vector from a Double3 object.
     *
     * @param xyz a Double3 object representing the x, y, and z components of the vector
     * @throws IllegalArgumentException if the Double3 object has all components equal to zero
     */

    Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector zero is illegal\n");

        }
    }


    /**
     * Returns a new Vector representing the vector addition of this vector and the specified vector.
     *
     * @param v0 the vector to add to this vector
     * @return the sum of this vector and the specified vector
     */

    public Vector add(Vector v0) {
        return new Vector(xyz.add(v0.xyz));
    }


    /**
     * Returns a new Vector representing the scaling of this vector by the specified scalar.
     *
     * @param rhs the scalar to multiply this vector by
     * @return the product of this vector and the scalar
     */

    public Vector scale(double rhs) {
        return new Vector(xyz.scale(rhs));
    }


    /**
     * Returns the dot product of this vector and the specified vector.
     *
     * @param v0 the vector to dot product with this vector
     * @return the dot product of this vector and the specified vector
     */
    public double dotProduct(Vector v0) {
        return xyz.d1 * v0.xyz.d1 + xyz.d2 * v0.xyz.d2 + xyz.d3 * v0.xyz.d3;
    }

    /**
     * Returns a new Vector representing the cross product of this vector and the specified vector.
     *
     * @param v0 the vector to cross product with this vector
     * @return the cross product of this vector and the specified vector
     */
    public Vector crossProduct(Vector v0) {
        return new Vector(new Double3(xyz.d2 * v0.xyz.d3 - xyz.d3 * v0.xyz.d2, xyz.d3 * v0.xyz.d1 - xyz.d1 * v0.xyz.d3, xyz.d1 * v0.xyz.d2 - xyz.d2 * v0.xyz.d1));
    }

    /**
     * Returns the square of the length of this vector.
     *
     * @return the square of the length of this vector
     */
    public double lengthSquared() {
        return xyz.d1 * xyz.d1 + xyz.d2 * xyz.d2 + xyz.d3 * xyz.d3;
    }

    /**
     * Returns the length of this vector.
     *
     * @return the length of this vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Returns a new Vector representing the normalization of this vector.
     *
     * @return the normalization of this vector
     */
    public Vector normalize() {
        //TODO: need to check what is better: 1. 3 primitives 2. Double3
        return new Vector(xyz.reduce(length()));
    }

        @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Vector other)
            return super.equals(other);
        return false;
    }
  /*  public String toString() {
        return "Vector: "+xyz;
    }*/
}

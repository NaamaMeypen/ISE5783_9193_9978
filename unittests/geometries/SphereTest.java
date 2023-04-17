package geometries;

import org.junit.jupiter.api.Test;
import primitives.point;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    /**
     * Test method for {@link Sphere#getNormal}
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Sphere s =new Sphere(1d,new point(0,0,1));
        assertDoesNotThrow( () ->s.getNormal(new point(0, 0, 2)), "");
        //TC02:
        assertThrows(IllegalArgumentException.class,() ->s.getNormal(new point(0,0,3)),
                "getNormal with a point that isnt on the sphere must throw an exception\n");

    }
}
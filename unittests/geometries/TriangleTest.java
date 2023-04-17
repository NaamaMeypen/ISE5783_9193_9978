package geometries;

import org.junit.jupiter.api.Test;
import primitives.Vector;
import primitives.point;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class TriangleTest {
    /**
     * Test method for {@link Triangle#getNormal}
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Triangle t = new Triangle(new point(0, 0, 1), new point(1, 0, 0), new point(0, 0, -1));
        // ensure there are no exceptions
        assertDoesNotThrow(() -> t.getNormal(new point(0, 0, 1)), "");
        // generate the test result
        Vector result = t.getNormal(new point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.lengthSquared(), 0.00000001, "Triangle's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        assertTrue(isZero(result.dotProduct(new point(0, 0, 1).subtract(new point(1, 0, 0)))),
                "Triangle's normal is not orthogonal to one of the edges");
        assertTrue(isZero(result.dotProduct(new point(0, 0, 1).subtract(new point(0, 0, -1)))),
                "Triangle's normal is not orthogonal to one of the edges");
        assertTrue(isZero(result.dotProduct(new point(1, 0, 0).subtract(new point(0, 0, -1)))),
                "Triangle's normal is not orthogonal to one of the edges");
    }


}
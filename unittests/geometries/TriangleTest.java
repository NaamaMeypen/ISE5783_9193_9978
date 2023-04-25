package geometries;

import org.junit.jupiter.api.Test;
import primitives.Ray;
import primitives.Vector;
import primitives.Point;

import java.util.List;

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
        Triangle t = new Triangle(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 0, -1));
        // ensure there are no exceptions
        assertDoesNotThrow(() -> t.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = t.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.lengthSquared(), 0.00000001, "Triangle's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        assertTrue(isZero(result.dotProduct(new Point(0, 0, 1).subtract(new Point(1, 0, 0)))),
                "Triangle's normal is not orthogonal to one of the edges");
        assertTrue(isZero(result.dotProduct(new Point(0, 0, 1).subtract(new Point(0, 0, -1)))),
                "Triangle's normal is not orthogonal to one of the edges");
        assertTrue(isZero(result.dotProduct(new Point(1, 0, 0).subtract(new Point(0, 0, -1)))),
                "Triangle's normal is not orthogonal to one of the edges");
    }


    /** Test method for {@link Triangle#findIntersections} */
    @Test
    void testFindIntsersections() {
        // ============ Equivalence Partitions Tests ==============
        //TC01: in triangle
        Triangle tr1 = new Triangle(new Point(0, 0, 1),
                new Point(1, 0, 0),
                new Point(0, 1, 0)
        );
        Ray ray1 = new Ray(new Point(0, 0, 0), new Vector(4, 3, 3));
        assertEquals(List.of(new Point(0.4, 0.3, 0.3)), tr1.findIntersections(ray1), "Bad intersection point to Triangle");

        //TC02: outside the triangle
        Triangle tr2 = new Triangle(new Point(-1, 0, 0),
                new Point(1, 0, 0),
                new Point(0, 0, 1)
        );
        Ray ray2 = new Ray(new Point(0, 1, -1), new Vector(0, -1, 0));
        assertNull(tr2.findIntersections(ray2), "Bad intersection point to Triangle");

        //TC03: outside the triangle between the edges
        Ray ray3 = new Ray(new Point(2, 2, 4), new Vector(-1, -1, -1));
        assertNull(tr2.findIntersections(ray3), "Bad intersection point to Triangle");

        // =============== Boundary Values Tests ==================
        //TC04: "intersect" point who is vertice
        Ray ray4 = new Ray(new Point(1, 1, 1), new Vector(-1, -1, 0));
        assertNull(tr2.findIntersections(ray4), "Bad intersection point to Triangle");

        //TC05: "intersect" point who is on edge
        Ray ray5 = new Ray(new Point(1, 1, 1), new Vector(-1, -1, -1));
        assertNull(tr2.findIntersections(ray5), "Bad intersection point to Triangle");

        //TC06: outside the triangle "intersect" point who is on edge's ray
        Ray ray6 = new Ray(new Point(-1,-1, -1), new Vector(1, 3, 1));
        assertNull(tr2.findIntersections(ray6), "Bad intersection point to Triangle");
    }
}
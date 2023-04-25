package geometries;

import org.junit.jupiter.api.Test;
import primitives.Ray;
import primitives.Vector;
import primitives.Point;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {
    void testConstructor()
    {
        // =============== Boundary Values Tests ==================
        // TC10: The first and second points are connected
        assertThrows(IllegalArgumentException.class,()->new Plane(new Point(1,2,3),new Point(1,2,3),
                new Point(1,1,1)),"\"Construct a plan with the first and second points are converge" +
                " must throw an exception\"");
         //TC20: three points that are on the same line
        assertThrows(IllegalArgumentException.class, ()->new Plane(new Point(1,2,3),new Point(2,4,6),
                new Point(3,6,9)),
                "Construct a plan with three points that are on the same line must throw an exception\"");
        // ============ Equivalence Partitions Tests ==============
        Plane p = new Plane(new Point(1,2,3),new Point(1,2,3),new Point(1,1,1));


    }

    /**
     * Test method for {@link Plane#getNormal}
     */
    @Test
    void testGetNormal() {
        Point p1 = new Point(1,2,0);
        Point p2 = new Point(4,-9,0);
        Point p3 = new Point(1,0,8);
        Plane p = new Plane(p1, p2, p3);
        Vector v1 = p1.subtract(p2);
        Vector v2 = p2.subtract(p3);
        Vector v3 = p3.subtract(p1);
        Vector n = p.getNormal();

        assertEquals(0d,v1.dotProduct(n),0.0001 ,"ERROR: incorrect normal for plane\n");
        assertEquals(0d,v2.dotProduct(n),0.0001 ,"ERROR: incorrect normal for plane\n");
        assertEquals(0d,v2.dotProduct(n),0.0001 ,"ERROR: incorrect normal for plane\n");
        assertEquals( 1d, n.lengthSquared(), 0.00001,"The normal have to be normalized\n");
    }

    /** Test method for {@link Plane#findIntersections} */
    @Test
    void testFindIntersections() {
        Plane pl = new Plane(new Point(0, 0, 1), new Vector(1, 1, 1));
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray into plane
        assertEquals(List.of(new Point(1, 0, 0)),
                pl.findIntersections(new Ray(new Point(0.5, 0, 0), new Vector(1, 0, 0))),
                "Bad plane intersection");

        // TC02: Ray out of plane
        assertNull(pl.findIntersections(new Ray(new Point(2, 0, 0), new Vector(1, 0, 0))),
                "Must not be plane intersection");

        // =============== Boundary Values Tests ==================
        // TC11: Ray parallel to plane
        assertNull(pl.findIntersections(new Ray(new Point(1, 1, 1), new Vector(0, 1, -1))),
                "Must not be plane intersection");

        // TC12: Ray in plane
        assertNull(pl.findIntersections(new Ray(new Point(0, 0.5, .5), new Vector(0, 1, -1))),
                "Must not be plane intersection");


        // TC13: Orthogonal ray start into plane
        assertNull(pl.findIntersections(new Ray(new Point(1, 1, -1), new Vector(0, 0, 1))),
                "Must not be plane intersection");

        // TC14: Orthogonal ray start after of plane

        assertNull(pl.findIntersections(new Ray(new Point(1,1,3), new Vector(0, 0, 1))),
                "Must not be plane intersection");

        // TC15: Orthogonal ray start before of plane
        assertEquals(List.of(new Point(1, 1, -1)),pl.findIntersections(new Ray(new Point(1, 1, -3),
                new Vector(0, 0, 1))),"Bad plane intersection");

        // TC16: Ray from plane
        assertNull(pl.findIntersections(new Ray(new Point(0, 0.5, 0.5), new Vector(1, 1, 0))),
                "Must not be plane intersection");

        // TC17: Ray from plane's Q point
        assertNull(pl.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 1, 0))),
                "Must not be plane intersection");
    }
    }
